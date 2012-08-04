/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.*;
import com.wadpam.rnr.dao.*;
import com.wadpam.rnr.domain.*;

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
    private DFavoritesDao favoritesDao;
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
    public DLike addLike(String productId, String username, String principalName, Float latitude, Float longitude) {
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

        return dLike;
    }


    // Get a like with a specific id
    public DLike getLike(long id) {
        LOG.debug("Get like with id " + id);

        DLike dLike = likeDao.findByPrimaryKey(id);

        return dLike;
    }

    // Delete a like with a specific id
    public DLike deleteLike(long id) {
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

        return dLike;
    }

    // Get all likes for a specific user
    public Collection<DLike> getMyLikes(String username, String principalName) {

        // Fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get all likes for user " + username);

        final Collection<DLike> myLikes = likeDao.findByUsername(username);

        return myLikes;
    }


    /* Rating related methods */

    // Rate a product
    public DRating addRating(String productId, String username, String principalName,
                             Float latitude, Float longitude, int rating, String comment) {
        LOG.debug("Add new rating to product " + productId);

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

        return dRating;
    }

    // Get a rating with a specific id
    public DRating getRating(long id) {
        LOG.debug("Get rating with id " + id);

        DRating dRating = ratingDao.findByPrimaryKey(id);

        return dRating;
    }

    // Delete a ratings with a specific id
    public DRating deleteRating(long id) {
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

        return dRating;
    }

    // Get all ratings done by a specific user
    public Collection<DRating> getMyRatings(String username, String principalName) {

        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get all ratings for user " + username);

        final Collection<DRating> dRatings = ratingDao.findByUsername(username);

        return dRatings;
    }


    /* Comment related methods */

    // Add a comment to a product
    public DComment addComment(String productId, String username, String principalName, Float latitude,
                               Float longitude, String comment) {
        LOG.debug("Add new comment to product " + productId);

        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        // Create new comment. Do not check if user have commented before
        DComment dComment = new DComment();
        dComment.setProductId(productId);
        dComment.setUsername(username);
        dComment.setComment(comment);
        commentDao.persist(dComment);

        // update product info
        DProduct dProduct = productDao.findByPrimaryKey(productId);
        if (null == dProduct) {
            dProduct = new DProduct();
            dProduct.setProductId(productId);
            dProduct.setCommentCount(1L);
        }
        else
            dProduct.setCommentCount(dProduct.getCommentCount() + 1);

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

        return dComment;
    }

    // Get a comment with a specific id
    public DComment getComment(long id) {
        LOG.debug("Get comment with id " + id);

        DComment dComment = commentDao.findByPrimaryKey(id);

        return dComment;
    }

    // Delete a comment with a specific id
    public DComment deleteComment(long id) {
        LOG.debug("Delete comment with id " + id);

        DComment dComment = commentDao.findByPrimaryKey(id);
        if (null == dComment)
            return null;

        // Delete the comment
        commentDao.delete(dComment);

        // Update the product
        DProduct dProduct = productDao.findByPrimaryKey(dComment.getProductId());
        if (null != dProduct) {
            dProduct.setCommentCount(dProduct.getCommentCount() - 1);
            persistenceManager.storeProductWithCache(dProduct);
        } else
            // Should not happen, log error
            LOG.error("Comment exist but not the product " + dComment.getProductId());

        return dComment;
    }

    // Get all comments done by a specific user
    public Collection<DComment> getMyComments(String username, String principalName) {

        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get all comments for user " + username);

        final Collection<DComment> dComments = commentDao.findByUsername(username);

        return dComments;
    }


    /* Favorite related methods */

    // Add new favorite product
    public DFavorites addFavorite(String productId, String username, String principalName) {
        LOG.debug("Add product " + productId + " as favorites for user " + username);

        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        // User name must be provided
        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        DFavorites dFavorites = favoritesDao.findByPrimaryKey(username); // TODO: Store favorites in the cache
        if (null == dFavorites) {
            // User does not have any existing favorites
            dFavorites = new DFavorites();
            dFavorites.setUsername(username);
            ArrayList<String> productIds = new ArrayList<String>(1);
            productIds.add(productId);
            dFavorites.setProductIds(productIds);
        } else
            // Update existing list of favorites if it is not already a favorite
            if (dFavorites.getProductIds().contains(productId) == false)
                dFavorites.getProductIds().add(productId);

        // Store
        favoritesDao.persist(dFavorites); // TODO: Store favorites in the cache

        return dFavorites;
    }

    // Delete a product from favorites
    public DFavorites deleteFavorite(String productId, String username, String principalName) {
        LOG.debug("Delete product " + productId + " from favorites for user "  + username);

        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        // User name must be provided
        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        DFavorites dFavorites = favoritesDao.findByPrimaryKey(username);
        // If the favorite is not found return null
        if (null == dFavorites || dFavorites.getProductIds().remove(productId) == false)
            return null;

        // Store
        favoritesDao.update(dFavorites);

        return dFavorites;
    }

    // Get all user favorites
    public DFavorites getFavorites(String username, String principalName) {
        LOG.debug("Get favorites for user "  + username);

        // fallback on principal name?
        if (null == username && fallbackPrincipalName) {
            username = principalName;
        }

        // User name must be provided
        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        DFavorites dFavorites = favoritesDao.findByPrimaryKey(username);

        return dFavorites;
    }


    /* Product related methods */

    // Get a specific product
    public DProduct getProduct(String productId) {
        LOG.debug("Get product " + productId);

        final DProduct dProduct = persistenceManager.getProductWithCache(productId);

        return dProduct;
    }

    // Get a list of products
    public Collection<DProduct> getProducts(String[] ids) {
        LOG.debug("Get a list of products " + ids);

        Map<String, DProduct> map = persistenceManager.getProductsWithCache(Arrays.asList(ids));

        return map.values();
    }

    // Get all products
    public String getProductPage(String cursor, int pageSize, Collection<DProduct> resultList) {
        LOG.debug("Get product page, cursor:" + cursor + " page size:" + pageSize);

       return persistenceManager.getProductPage(cursor, pageSize, resultList);
    }

    // Find nearby products with different sort order
    public Collection<DProduct> findNearbyProducts(Float latitude, Float longitude, int bits, int sortOrder, int limit) {
        // TODO: The location should be a property on DResult (not DRating). Needs refactoring
        //final Collection<DRating> list = geoRatingDao.findInGeobox(latitude, longitude, bits, ratingDao.COLUMN_NAME_RATING, false, 0, 10);

        // TODO: Implement sort order

        // TODO: Add max number of hits to return

        throw new UnsupportedOperationException("Not yet implemented");
    }


    // Find nearby products and return in KML format
    public void findNearbyProductsKml(Float latitude, Float longitude, int bits, int sortOrder, int limit, PrintWriter out) {
        Collection<DProduct> productList = findNearbyProducts(latitude, longitude, bits, limit, sortOrder);
        writeRatingsKml(out, productList);
    }

    // Various help methods for generating KML format
    protected void writeRatingsKml(PrintWriter kmlDest, Collection<DProduct> products) {
        kmlDest.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        kmlDest.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
        kmlDest.println("<Document>");
        kmlDest.println("   <name>Nearby results</name>");
        kmlDest.println("   <description>This is the description tag of the KML document</description>");

        for (DProduct product : products) {
            writePlacemarkKml(kmlDest, product);
        }

        kmlDest.println("</Document>");
        kmlDest.println("</kml>");
    }

    protected void writePlacemarkKml(PrintWriter kmlDest, DProduct product) {
        if (null != product.getLocation()) {
            kmlDest.println("   <Placemark>");
            kmlDest.println("      <name>" + product.getId() + "</name>");
    //                        kmlDest.println("      <address>" + area + ", " + loc + "</address>");

            StringBuffer desc = new StringBuffer("<![CDATA[rating: ");
            desc.append(product.getRatingAverage());
            desc.append("/100]]>");
            // TODO: include like and comment information in the KML

            kmlDest.println("      <description>" + desc.toString() + "</description>");
            kmlDest.println("      <Point>");
            kmlDest.println(String.format("         <coordinates>%s,%s,0</coordinates>",
                    Float.toString(product.getLocation().getLongitude()),
                    Float.toString(product.getLocation().getLatitude())));
            kmlDest.println("      </Point>");
            kmlDest.println("   </Placemark>");
        }
    }

    // Get the most liked products
    public Collection<DProduct> getMostLikedProducts(int limit) {
        LOG.debug("Get most liked products");

        // TODO: Implementation missing
        //Collection<DProduct> dProducts = productDao.

        throw new UnsupportedOperationException("Not yet implemented");
    }

     // Get all likes for a product
    public Collection<DLike> getAllLikesForProduct(String productId) {
        LOG.debug("Get all likes for product " + productId);

        Collection<DLike> dLikes = likeDao.findByProductId(productId);

        return dLikes;
    }

    // Get all ratings for a product
    public Collection<DRating> getAllRatingsForProduct(String productId) {
        LOG.debug("Get all ratings for product " + productId);

        Collection<DRating> dRatings = ratingDao.findByProductId(productId);

        return dRatings;
    }

    // Get all comments for a product
    public Collection<DComment> getAllCommentsForProduct(String productId) {
        LOG.debug("Get all comments for product " + productId);

        Collection<DComment> dComments = commentDao.findByProductId(productId);

        return dComments;
    }


    // Get all products a user have liked
    public Collection<DProduct> getProductsLikedByUser(String username, String principalName) {

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

        final Collection<DProduct> dProducts = productDao.findByPrimaryKeys(productIds).values(); // TODO: Use cache

        return dProducts;
    }

    // Get all products a user have rated
    public Collection<DProduct> getProductsRatedByUser(String username, String principalName) {

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

        final Collection<DProduct> dProducts = productDao.findByPrimaryKeys(productIds).values(); // TODO: Use cache

        return dProducts;
    }

    // Get all products a user have commented
    public Collection<DProduct> getProductsCommentedByUser(String username, String principalName) {

        // Fallback on principal name?
        if (null == username && fallbackPrincipalName)
            username = principalName;

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get all products commented by user " + username);

        final Collection<DComment> myComments = commentDao.findByUsername(username);

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>(myComments.size());
        for (DComment dComment : myComments)
            productIds.add(dComment.getProductId());

        final Collection<DProduct> dProducts = productDao.findByPrimaryKeys(productIds).values(); // TODO: Use cache

        return dProducts;
    }

    // Get all users favorite products
    public Collection<DProduct> geUserFavoriteProducts(String username, String principalName) {

        // Fallback on principal name?
        if (null == username && fallbackPrincipalName)
            username = principalName;

        if (null == username)
            throw new IllegalArgumentException("Username must be specified or authenticated");

        LOG.debug("Get favorite products for user " + username);

        final DFavorites dFavorites = favoritesDao.findByPrimaryKey(username);

        final Collection<DProduct> dProducts = productDao.findByPrimaryKeys(dFavorites.getProductIds()).values(); // TODO: Use cache

        return  dProducts;
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

    public void setFavoritesDao(DFavoritesDao favoritesDao) {
        this.favoritesDao = favoritesDao;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public static void setFallbackPrincipalName(boolean fallbackPrincipalName) {
        RnrService.fallbackPrincipalName = fallbackPrincipalName;
    }
}
