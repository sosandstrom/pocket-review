/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.service;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Rating;
import com.wadpam.rnr.dao.DRatingDao;
import com.wadpam.rnr.dao.DResultDao;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.domain.DResult;
import com.wadpam.rnr.json.*;
import java.io.PrintWriter;
import java.util.*;
import net.sf.mardao.api.dao.Expression;
import net.sf.mardao.api.domain.AEDPrimaryKeyEntity;
import net.sf.mardao.api.geo.aed.GeoDao;
import net.sf.mardao.api.geo.aed.GeoDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author os
 */
public class RnrService {
    static final Logger LOG = LoggerFactory.getLogger(RnrService.class);
    
    private static boolean fallbackPrincipalName = true;

    private DRatingDao ratingDao;
    private DResultDao resultDao;
    private GeoDao geoRatingDao;
    
    public void init() {
        geoRatingDao = new GeoDaoImpl<DRating, DRating>(ratingDao);

//        doInDomain("dev", new Runnable() {
//
//            @Override
//            public void run() {
//                for (DRating rating : ratingDao.findAll()) {
//                    if (null != rating.getLocation()) {
//                        geoRatingDao.save(rating);
//                    }
//                }
//            }
//            
//        });
    }

    // TODO: Not used?
    public static void doInDomain(String domain, Runnable task) {
        final String currentNamespace = NamespaceManager.get();
        try {
            NamespaceManager.set(domain);
            task.run();
        }
        finally {
            NamespaceManager.set(currentNamespace);
        }
    }


    // Add a rating to a product
    public JResult addRating(String productId, String username,
            String principalName,
            Float latitude, Float longitude, int rating) {
        
        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }
        
        DRating dr = null;
        int existing = -1;
        
        // specified user can only rate once
        // TODO: This should actually be a setting per domain.
        if (null != username) {
            dr = ratingDao.findByProductIdUsername(productId, username);
        }
        
        // create new?
        final boolean create = null == dr;
        if (create) {
            dr = new DRating();
            dr.setProductId(productId);
            dr.setUsername(username);
        }
        else {
            // store existing rating to subtract below
            existing = dr.getRating().getRating();
        }
        
        // update
        // TODO: location should be on the DResult object
        dr.setRating(new Rating(rating));
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            dr.setLocation(location);
            geoRatingDao.save(dr);
        }
        else {
            ratingDao.persist(dr);
        }
        
        // update average result
        DResult result = resultDao.findByPrimaryKey(productId);
        if (null == result) {
            result = new DResult();
            result.setProductId(productId);
            result.setRatingCount(1L);
            result.setRatingSum((long)rating);
        }
        else {
            // result exists, and existing rating for user
            if (-1 < existing) {
                result.setRatingSum(result.getRatingSum() - existing + rating);
                // no need to update ratingCount!
            }
            else {
                // result exists, no existing rating for user
                result.setRatingSum(result.getRatingSum() + rating);
                result.setRatingCount(result.getRatingCount() + 1);
            }
            
        }
        // TODO: Not thread safe. Make the update in a Task to ensure serialization?
        resultDao.persist(result);
        
        return Utils.convert(result);
    }

    // Get the average rating for a specific product
    public JResult getAverage(String productId) {
        final DResult result = resultDao.findByPrimaryKey(productId);

        return Utils.convert(result);
    }


    // Get the average rating for a a list of products
    public Collection<JResult> getAverageRatings(String[] ids) {
        Map<String,DResult> map = resultDao.findByPrimaryKeys(null, Arrays.asList(ids));
        return (Collection<JResult>) Utils.convert(map.values());
    }

    // Get the average rating for all products
    public JResultPage getAveragePage(String cursor, long offset, long limit) {
        // TODO: Implementation missing
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Get all ratings done by a specific user
    public Collection<JRating> getMyRatings(String username, String principalName) {
        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        if (null == username) {
            throw new IllegalArgumentException("Username must be specified or authenticated");
        }

        final List<DRating> list = ratingDao.findByUsername(username);
        return (Collection<JRating>) Utils.convert(list);
    }

    // Get the average rating of a list of Ratings
    // TODO: This method can be removed after refactoring the findNearbyRatings methods below
    protected Collection<JResult> average(Collection<JRating> jRatings) {
        // collect ids
        final String ids[] = new String[jRatings.size()];
        int i = 0;
        for (JRating rating : jRatings) {
            ids[i++] = rating.getProductId();
        }
        LOG.debug("ratings productIds={}", ids);

        // load results
        final Map<String,DResult> dMap = resultDao.findByPrimaryKeys(null, Arrays.asList(ids));
        final Collection<JResult> returnValue = new ArrayList<JResult>(jRatings.size());

        // convert and add location info
        for (JRating rating : jRatings) {
            DResult d = dMap.get(rating.getProductId());
            if (null != d) {
                JResult j = Utils.convert(d);
                j.setLocation(rating.getLocation());
                LOG.debug("   populating {} with location {}", j.getId(), j.getLocation());
                returnValue.add(j);
            }
        }

        return returnValue;
    }

    // Find nearby average ratings
    public Collection<JResult> findNearbyRatings(Float latitude, Float longitude, int bits) {
        // TODO: Should return DResult
        final Collection<DRating> list = geoRatingDao.findInGeobox(latitude, longitude, bits,
                ratingDao.COLUMN_NAME_RATING, false, 0, 10);

        final Collection<JRating> jRatings = (Collection<JRating>) Utils.convert(list);

        return average(jRatings);
    }

    // Find nearby average ratings and return in KML format
    public void nearbyRatingsKml(Float latitude, Float longitude, int bits, PrintWriter out) {
        Collection<JResult> list = findNearbyRatings(latitude, longitude, bits);
        writeRatingsKml(out, list);
    }

    // Various help methods for generating KML format
    protected void writeRatingsKml(PrintWriter kmlDest, Collection<JResult> list) {
        kmlDest.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        kmlDest.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
        kmlDest.println("<Document>");
        kmlDest.println("   <name>Nearby ratings</name>");
        kmlDest.println("   <description>This is the description tag of the KML document</description>");

        for (JResult result : list) {
            writePlacemarkKml(kmlDest, result);
        }

        kmlDest.println("</Document>");
        kmlDest.println("</kml>");
    }
    
    protected void writePlacemarkKml(PrintWriter kmlDest, JResult result) {
        if (null != result.getLocation()) {
            kmlDest.println("   <Placemark>");
            kmlDest.println("      <name>" + result.getId() + "</name>");
    //                        kmlDest.println("      <address>" + area + ", " + loc + "</address>");

            StringBuffer desc = new StringBuffer("<![CDATA[rating: ");
            desc.append(result.getAverage());
            desc.append("/100]]>");
    
            kmlDest.println("      <description>" + desc.toString() + "</description>");
            kmlDest.println("      <Point>");
            kmlDest.println(String.format("         <coordinates>%s,%s,0</coordinates>", 
                    Float.toString(result.getLocation().getLongitude()), 
                    Float.toString(result.getLocation().getLatitude())));
            kmlDest.println("      </Point>");
            kmlDest.println("   </Placemark>");
        }        
    }

    public void setRatingDao(DRatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public void setResultDao(DResultDao resultDao) {
        this.resultDao = resultDao;
    }

    public static void setFallbackPrincipalName(boolean fallbackPrincipalName) {
        RnrService.fallbackPrincipalName = fallbackPrincipalName;
    }

}
