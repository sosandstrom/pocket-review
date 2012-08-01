/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.*;
import com.wadpam.rnr.dao.DCommentDao;
import com.wadpam.rnr.dao.DLikeDao;
import com.wadpam.rnr.dao.DProductDao;
import com.wadpam.rnr.dao.DRatingDao;
import com.wadpam.rnr.domain.DComment;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.domain.DProduct;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.json.*;
import java.io.PrintWriter;
import java.util.*;

import net.sf.mardao.api.geo.aed.GeoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author os
 */
public class RnrService {
    static final Logger LOG = LoggerFactory.getLogger(RnrService.class);
    
    private static boolean fallbackPrincipalName = true;

    // TODO: This should be a setting that the admin can do per project
    private static boolean onlyRateOncePerUser = true;

    private PersistenceManager persistenceManager;

    private DProductDao productDao;
    private DRatingDao ratingDao;
    private DLikeDao likeDao;
    private DCommentDao commentDao;
    private GeoDao geoResultDao;


    public void init() {
        //geoResultDao = new GeoDaoImpl<DProduct, DProduct>(productDao);  // TODO: Uncomment once the DResult implement GeoModel

//        doInDomain("dev", new Runnable() { // TODO: Not used remove?
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

//    // TODO: Not used. Remove?
//    public static void doInDomain(String domain, Runnable task) {
//        final String currentNamespace = NamespaceManager.get();
//        try {
//            NamespaceManager.set(domain);
//            task.run();
//        }
//        finally {
//            NamespaceManager.set(currentNamespace);
//        }
//    }


    /* Like related methods */

    // Like a product
    public JLike addLike(String productId, String username, String principalName, Float latitude, Float longitude) {
        LOG.debug("Add new like to product " + productId);

        // Fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        // Specified user can only Like once
        DLike dLike = null;
        if (onlyRateOncePerUser && null != username) {
            dLike = likeDao.findByProductIdUsername(productId, username);
        }

        // Create new?
        final boolean create = null == dLike;
        if (create) {
            dLike = new DLike();
            dLike.setProductId(productId);
            dLike.setUsername(username);
        }  else {
            // The user already Liked this product
            LOG.debug("User" + username + " already liked this product");
            // TODO: Return dedicated http code
        }

        // Store the like
        likeDao.persist(dLike);

        // Update total number of likes
        DProduct dProduct = productDao.findByPrimaryKey(productId);
        if (null == dProduct) {
            // First time this product is handled, create new
            dProduct = new DProduct();
            dProduct.setProductId(productId);
            dProduct.setLikeCount(1L);
        } else {
            // Update existing
            dProduct.setLikeCount(dProduct.getLikeCount() + 1);
        }

        // Update the product location if any is provided
        // TODO: DB write not thread safe
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            dProduct.setLocation(location);
            //geoResultDao.save(dResult);   // TODO: Uncomment once geoResultDao is declared
        }
        else {
            persistenceManager.storeProductWithCache(dProduct);
        }

        return Converters.convert(dLike);
    }


    // Get a like with a specific id
    public JLike getLike(long id) {
        LOG.debug("Get a like with id " + id);

        DLike dLike = likeDao.findByPrimaryKey(id);

        return Converters.convert(dLike);
    }

    // Delete a like with a specific id
    public JLike deleteLike(long id) {
        LOG.debug("Delete like with id " + id);

        DLike dLike = likeDao.findByPrimaryKey(id);
        if (null == dLike)
            return null;

        // Delete the like
        likeDao.delete(dLike);

        // Update the product
        DProduct dProduct = productDao.findByPrimaryKey(dLike.getProductId());
        if (null != dProduct) {
            dProduct.setLikeCount(dProduct.getLikeCount() - 1);
            persistenceManager.storeProductWithCache(dProduct);
        } else
            // Should not happen, log error
            LOG.error("Like exist but not the product " + dLike.getProductId());

        return Converters.convert(dLike);
    }

    // Get all likes for a specific user
    public Collection<JLike> getMyLikes(String username, String principalName) {

        // Fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get all likes for user " + username);

        final List<DLike> myLikes = likeDao.findByUsername(username);

        return (Collection<JLike>)Converters.convert(myLikes);
    }


    /* Rating related methods */

    // Rate a product
    public JRating addRating(String productId, String username, String principalName,
                             Float latitude, Float longitude, int rating, String comment) {

        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        DRating dRating = null;
        int existing = -1;

        // specified user can only rate once
        if (onlyRateOncePerUser && null != username) {
            dRating = ratingDao.findByProductIdUsername(productId, username);
        }

        // create new?
        final boolean create = null == dRating;
        if (create) {
            dRating = new DRating();
            dRating.setProductId(productId);
            dRating.setUsername(username);
        }
        else {
            // store existing rating to subtract below
            existing = dRating.getRating().getRating();
        }

        // store the rating  (always update the rating and review comment)
        dRating.setRating(new Rating(rating));
        dRating.setComment(comment);
        ratingDao.persist(dRating);

        // update product info
        DProduct dProduct = productDao.findByPrimaryKey(productId);
        if (null == dProduct) {
            dProduct = new DProduct();
            dProduct.setProductId(productId);
            dProduct.setRatingCount(1L);
            dProduct.setRatingSum((long) rating);
        }
        else {
            // result exists, and existing rating for user
            if (-1 < existing) {
                dProduct.setRatingSum(dProduct.getRatingSum() - existing + rating);
                // no need to update ratingCount!
            }
            else {
                // result exists, no existing rating for user
                dProduct.setRatingSum(dProduct.getRatingSum() + rating);
                dProduct.setRatingCount(dProduct.getRatingCount() + 1);
            }

        }

        // Update product location if provided in the request
        // TODO: DB write not thread safe
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            dProduct.setLocation(location);
            //geoResultDao.save(dProduct);   // TODO: Uncomment once geoResultDao is declared
        }
        else {
            persistenceManager.storeProductWithCache(dProduct);
        }

        return Converters.convert(dRating);
    }

    // Get a rating with a specific id
    public JRating getRating(long id) {
        LOG.debug("Get a rating with id " + id);

        DRating dLike = ratingDao.findByPrimaryKey(id);

        return Converters.convert(dLike);
    }

    // Delete a ratings with a specific id
    public JRating deleteRating(long id) {
        LOG.debug("Delete ratings with id " + id);

        DRating dRating = ratingDao.findByPrimaryKey(id);
        if (null == dRating)
            return null;

        // Delete the rating
        ratingDao.delete(dRating);

        // Update the product
        DProduct dProduct = productDao.findByPrimaryKey(dRating.getProductId());
        if (null != dProduct) {
            dProduct.setRatingSum(dProduct.getRatingSum() - dRating.getRating().getRating());
            dProduct.setRatingCount(dProduct.getRatingCount() - 1);
            persistenceManager.storeProductWithCache(dProduct);
        } else
            // Should not happen, log error
            LOG.error("Rating exist but not the product " + dRating.getProductId());

        return Converters.convert(dRating);
    }

    // Get all ratings done by a specific user
    public Collection<JRating> getMyRatings(String username, String principalName) {

        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get all ratings for user " + username);

        final Collection<DRating> myRatings = ratingDao.findByUsername(username);

        return (Collection<JRating>)Converters.convert(myRatings);
    }


    /* Product related methods */

    // Get a specific product
    public JProductV15 getProduct(String productId) {
        LOG.debug("Get product " + productId);

        final DProduct dProduct = persistenceManager.getProductWithCache(productId);

        return Converters.convert(dProduct);
    }

    // Get a list of products
    public Collection<JProductV15> getProducts(String[] ids) {
        LOG.debug("Get a list of products " + ids);

        Map<String, DProduct> map = persistenceManager.getProductsWithCache(Arrays.asList(ids));

        return (Collection<JProductV15>)Converters.convert(map.values());
    }

    // Get all products
    public JProductPage getProductPage(String cursor, int pageSize) {
        LOG.debug("Get product page, cursor:" + cursor + " page size:" + pageSize);

        Collection<DProduct> dProducts = new ArrayList<DProduct>(pageSize);
        String newCursor = persistenceManager.getProductPage(cursor, pageSize, dProducts);

        if (dProducts.size() == 0)
            return null;

        // Create a Json response
        JProductPage productPage = new JProductPage();
        productPage.setCursor(newCursor);
        productPage.setPageSize(pageSize);
        productPage.setProducts((Collection<JProductV15>) Converters.convert(dProducts));

       return productPage;
    }

    // Find nearby products with different sort order
    public Collection<JProductV15> findNearbyProducts(Float latitude, Float longitude, int bits, int sortOrder, int limit) {
        // TODO: The location should be a property on DResult (not DRating). Needs refactoring
        //final Collection<DRating> list = geoRatingDao.findInGeobox(latitude, longitude, bits, ratingDao.COLUMN_NAME_RATING, false, 0, 10);

        // TODO: Implement sort order

        // TODO: Add max number of hits to return

        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Find nearby products and return in KML format
    public void findNearbyProductsKml(Float latitude, Float longitude, int bits, int sortOrder, int limit, PrintWriter out) {
        Collection<JProductV15> list = findNearbyProducts(latitude, longitude, bits, limit, sortOrder);
        writeRatingsKml(out, list);
    }

    // Various help methods for generating KML format
    protected void writeRatingsKml(PrintWriter kmlDest, Collection<JProductV15> list) {
        kmlDest.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        kmlDest.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
        kmlDest.println("<Document>");
        kmlDest.println("   <name>Nearby results</name>");
        kmlDest.println("   <description>This is the description tag of the KML document</description>");

        for (JProductV15 result : list) {
            writePlacemarkKml(kmlDest, result);
        }

        kmlDest.println("</Document>");
        kmlDest.println("</kml>");
    }

    protected void writePlacemarkKml(PrintWriter kmlDest, JProductV15 result) {
        if (null != result.getLocation()) {
            kmlDest.println("   <Placemark>");
            kmlDest.println("      <name>" + result.getId() + "</name>");
    //                        kmlDest.println("      <address>" + area + ", " + loc + "</address>");

            StringBuffer desc = new StringBuffer("<![CDATA[rating: ");
            desc.append(result.getRatingAverage());
            desc.append("/100]]>");
            // TODO: include like and comment information in the KML

            kmlDest.println("      <description>" + desc.toString() + "</description>");
            kmlDest.println("      <Point>");
            kmlDest.println(String.format("         <coordinates>%s,%s,0</coordinates>",
                    Float.toString(result.getLocation().getLongitude()),
                    Float.toString(result.getLocation().getLatitude())));
            kmlDest.println("      </Point>");
            kmlDest.println("   </Placemark>");
        }
    }

    // Get the most liked products
    public Collection<JProductV15> getMostLikedProducts(int limit) {
        LOG.debug("Get most liked products");

        // TODO: Implementation missing
        //Collection<DProduct> dProducts = productDao.


        throw new UnsupportedOperationException("Not yet implemented");
    }

     // Get all likes for a product
    public Collection<JLike> getAllLikesForProduct(String productId, String principalName) {
        LOG.debug("Get all likes for product " + productId);

        Collection<DLike> dLikes = likeDao.findByProductId(productId);

        return (Collection<JLike>)Converters.convert(dLikes);
    }

    // Get all ratings for a product
    public Collection<JRating> getAllRatingsForProduct(String productId, String principalName) {
        LOG.debug("Get all ratings for product " + productId);

        Collection<DRating> dRatings = ratingDao.findByProductId(productId);

        return (Collection<JRating>)Converters.convert(dRatings);
    }

    // Get all products a user have liked
    public Collection<JProductV15> getProductsLikedByUser(String username, String principalName) {

        // Fallback on principal name?
        if (null == username && fallbackPrincipalName)
            username = principalName;

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get all products liked by user " + username);

        final Collection<DLike> myLikes = likeDao.findByUsername(username);

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>(myLikes.size());
        for (DLike like : myLikes)
            productIds.add(like.getProductId());

        final Collection<DProduct> dProducts = productDao.findByPrimaryKeys(null, productIds).values();

        return (Collection<JProductV15>)Converters.convert(dProducts);
    }


    // Get all products a user have rated
    public Collection<JProductV15> getProductsRatedByUser(String username, String principalName) {
        LOG.debug("Get all products rated by user " + username);

        // Fallback on principal name?
        if (null == username && fallbackPrincipalName)
            username = principalName;

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get all products rated by user " + username);

        final Collection<DRating> myRatings = ratingDao.findByUsername(username);

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>(myRatings.size());
        for (DRating rating : myRatings)
            productIds.add(rating.getProductId());

        final Collection<DProduct> dProducts = productDao.findByPrimaryKeys(null, productIds).values();

        return (Collection<JProductV15>)Converters.convert(dProducts);
    }

    // Setters and getters
    public void setRatingDao(DRatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public void setProductDao(DProductDao productDao) {
        this.productDao = productDao;
    }

    public void setLikeDao(DLikeDao likeDao) {
        this.likeDao = likeDao;
    }

    public void setCommentDao(DCommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public static void setFallbackPrincipalName(boolean fallbackPrincipalName) {
        RnrService.fallbackPrincipalName = fallbackPrincipalName;
    }
}
