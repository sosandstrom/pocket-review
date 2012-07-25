package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.GeoPt;
import com.wadpam.rnr.dao.DResultDao;
import com.wadpam.rnr.dao.DLikeDao;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.domain.DResult;
import com.wadpam.rnr.json.JResult;
import net.sf.mardao.api.geo.aed.GeoDao;
import net.sf.mardao.api.geo.aed.GeoDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/24/12
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class LikeService {

    static final Logger LOG = LoggerFactory.getLogger(LikeService.class);

    private static boolean fallbackPrincipalName = true;

    private DResultDao resultDao;
    private DLikeDao likeDao;
    private GeoDao geoResultDao;


    public void init() {
        //geoResultDao = new GeoDaoImpl<DResult, DResult>(resultDao);
    }


    // Add a new like a product
    public JResult addLike(String productId, String username, String principalName, Float latitude, Float longitude) {
        LOG.debug("Add new Like to product " + productId);

        // Fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        // Specified user can only Like once
        DLike dLike = null;
        // TODO: This should actually be a setting per domain, true or false
        if (null != username) {
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
            LOG.debug("User" + username + " already Liked this product");
            // TODO: Return dedicated http code
        }

        likeDao.persist(dLike);

        // Update total number of likes
        DResult dResult = resultDao.findByPrimaryKey(productId);
        if (null == dResult) {
            // First time this product is handled, create new
            dResult = new DResult();
            dResult.setProductId(productId);
            dResult.setNumberOfLikes(1L);
        } else {
            // Update existing
            dResult.setNumberOfLikes(dResult.getNumberOfLikes() + 1);
        }

        // Update the product location if any is provided
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            // TODO: Set the location on the dResult
            //dResult.setLocation(location);
        }

        // TODO: Not thread safe
        resultDao.persist(dResult);

        return Utils.convert(dResult);
    }


    // Get the number of Likes for a product
    public JResult getNumberOfLikes(String productId) {
        LOG.debug("Get number of Likes for product " + productId);

        final DResult dResult = resultDao.findByPrimaryKey(productId);

        return Utils.convert(dResult);
    }


    // Setters for Spring
    public static void setFallbackPrincipalName(boolean fallbackPrincipalName) {
        LikeService.fallbackPrincipalName = fallbackPrincipalName;
    }

    public void setResultDao(DResultDao resultDao) {
        this.resultDao = resultDao;
    }

    public void setLikeDao(DLikeDao likeDao) {
        this.likeDao = likeDao;
    }
}
