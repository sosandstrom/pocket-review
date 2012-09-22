package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.*;
import com.wadpam.open.transaction.Idempotent;
import com.wadpam.rnr.dao.*;
import com.wadpam.rnr.domain.*;

import java.io.PrintWriter;
import java.util.*;

import com.wadpam.server.exceptions.BadRequestException;
import com.wadpam.server.exceptions.NotFoundException;
import net.sf.mardao.api.geo.aed.GeoDao;
import net.sf.mardao.api.geo.aed.GeoDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author os
 */
public class RnrService {
    static final Logger LOG = LoggerFactory.getLogger(RnrService.class);

    private DAppDao appDao;
    private DAppSettingsDao appSettingsDao;
    private DProductDao productDao;
    private DRatingDao ratingDao;
    private DLikeDao likeDao;
    private DThumbsDao thumbsDao;
    private DCommentDao commentDao;
    private DFavoritesDao favoritesDao;


    // enum to hold thumbs up and down
    public enum Thumbs {
        UP(1), DOWN(-1);


        private int value;

        private Thumbs(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }

    }
    public void init() {
        // Do nothing
    }


    /* Like related methods */


    // Like a product
    @Idempotent
    @Transactional
    public DLike addLike(String domain, String productId, String username, Float latitude, Float longitude) {
        LOG.debug("Add new like to product:{}", productId);

        // Specified users can only Like once
        DAppSettings settings = appSettingsDao.findByPrimaryKey(domain);
        boolean onlyLikeOncePerUser = (null != settings) ? settings.getOnlyLikeOncePerUser() : true;

        DLike dLike = null;
        if (onlyLikeOncePerUser && null != username) {
            dLike = likeDao.findByProductIdUsername(productId, username);
        }

        // Create new?
        final boolean create = null == dLike;
        if (create) {
            dLike = new DLike();
            dLike.setProductId(productId);
            dLike.setUsername(username);
            // Store
            likeDao.persist(dLike);
        }  else {
            LOG.debug("User:{} already liked this product", username);
            // Do not increase the like count, just return th existing like
            return dLike;
        }

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
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            dProduct.setLocation(location);
        }

        // Persist and index the location
        productDao.persistAndIndexLocation(dProduct);

        // Call to generate a datastrore ConcurrentModificationException in order to test the transaction retry mechanism
        // Uncomment this to run tests
        //throw(new ConcurrentModificationException());

        return dLike;
    }

    // Get a like with a specific id
    public DLike getLike(long id) {
        LOG.debug("Get like with id:{}", id);

        DLike dLike = likeDao.findByPrimaryKey(id);

        if (null == dLike)
            throw new NotFoundException(404, String.format("Like with id:%s not found", id));

        return dLike;
    }


    // Delete a like with a specific id
    @Idempotent
    @Transactional
    public DLike deleteLike(long id) {
        LOG.debug("Delete like with id:{}", id);

        DLike dLike = likeDao.findByPrimaryKey(id);
        if (null == dLike)
            throw new NotFoundException(404, String.format("Like with id:%s not found", id));

        // Delete the like
        likeDao.delete(dLike);

        // Update the product
        DProduct dProduct = productDao.findByPrimaryKey(dLike.getProductId());
        if (null != dProduct) {
            dProduct.setLikeCount(dProduct.getLikeCount() - 1);
            productDao.persist(dProduct);
        } else
            // Should not happen, log error
            LOG.error("Like exist but not the product:{}", dLike.getProductId());

        return dLike;
    }

    // Get all likes for a specific user
    public Collection<DLike> getMyLikes(String username) {
        LOG.debug("Get all likes for user:{}", username);

        final Collection<DLike> myLikes = likeDao.findByUsername(username);

        return myLikes;
    }


    /* Thumbs related methods */


    // Add a thumbs up or down
    @Idempotent
    @Transactional
    public DThumbs addThumbs(String domain, String productId, String username, Float latitude, Float longitude, Thumbs value) {

        LOG.debug("Add new thumbs:{} to product:{}", value, productId);

        // Specified users can only thumb once
        DAppSettings settings = appSettingsDao.findByPrimaryKey(domain);
        boolean onlyThumbOncePerUser = (null != settings) ? settings.getOnlyThumbOncePerUser() : true;

        DThumbs dThumbs = null;
        if (onlyThumbOncePerUser && null != username) {
            dThumbs = thumbsDao.findByProductIdUsername(productId, username);
        }

        // Remember the existing value
        Thumbs existing = null;

        // Create new?
        final boolean create = null == dThumbs;
        if (create) {
            dThumbs = new DThumbs();
            dThumbs.setProductId(productId);
            dThumbs.setUsername(username);
        } else
            existing = (dThumbs.getValue()) > 1 ? Thumbs.UP : Thumbs.DOWN;

        // Always update the value
        dThumbs.setValue((long)value.getValue());

        // Store
        thumbsDao.persist(dThumbs);

        // Update total number of thumbs
        DProduct dProduct = productDao.findByPrimaryKey(productId);
        if (null == dProduct) {
            // First time this product is handled, create new
            dProduct = new DProduct();
            dProduct.setProductId(productId);
            if (value == Thumbs.UP)
                dProduct.setThumbsUp(1L);
            else
                dProduct.setThumbsDown(1L);
        } else {
            if (null == existing) {
                // User has not thumbed before
                if (value == Thumbs.UP)
                    dProduct.setThumbsUp(dProduct.getThumbsUp() + 1);
                else
                    dProduct.setThumbsDown(dProduct.getThumbsDown() + 1);
            } else {
                // User has thumbed before
                if (existing != value) {
                    // User has not thumbed before
                    if (value == Thumbs.UP) {
                        dProduct.setThumbsUp(dProduct.getThumbsUp() + 1);
                        dProduct.setThumbsDown(dProduct.getThumbsDown() - 1);
                    }
                    else {
                        dProduct.setThumbsDown(dProduct.getThumbsDown() + 1);
                        dProduct.setThumbsUp(dProduct.getThumbsUp() - 1);
                    }
                }
            }
        }

        // Update the product location if any is provided
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            dProduct.setLocation(location);
        }

        // Persist and index the location
        productDao.persistAndIndexLocation(dProduct);

        return dThumbs;
    }

    // Delete a specific thumbs
    @Idempotent
    @Transactional
    public DThumbs deleteThumbs(long id) {
        LOG.debug("Delete thumb with id:{}", id);

        DThumbs dThumbs = thumbsDao.findByPrimaryKey(id);
        if (null == dThumbs)
            throw new NotFoundException(404, String.format("Thumb with id:%s not found", id));

        // Delete
        thumbsDao.delete(dThumbs);

        // Update the product
        DProduct dProduct = productDao.findByPrimaryKey(dThumbs.getProductId());
        if (null != dProduct) {

            // Figure out what value to decrease
            if (dThumbs.getValue() > 0)
                dProduct.setThumbsUp(dProduct.getThumbsUp() - 1);
            else
                dProduct.setThumbsDown(dProduct.getThumbsDown() - 1);

            productDao.persist(dProduct);
        } else
            // Should not happen, log error
            LOG.error("Thumb exist but not the product:{}", dThumbs.getProductId());

        return dThumbs;
    }

    // Get a specific thumbs
    public DThumbs getThumbs(long id) {
        LOG.debug("Get thumb with id:{}", id);

        DThumbs dThumb = thumbsDao.findByPrimaryKey(id);

        if (null == dThumb)
            throw new NotFoundException(404, String.format("Thumb with id:%s not found", id));

        return dThumb;
    }

    // Get my thumbs for user
    public Collection<DThumbs> getMyThumbs(String username) {
        LOG.debug("Get all thumbs for user:{}", username);

        final Collection<DThumbs> myThumbs = thumbsDao.findByUsername(username);

        return myThumbs;
    }

    /* Rating related methods */


    // Rate a product
    @Idempotent
    @Transactional
    public DRating addRating(String domain, String productId, String username,
                             Float latitude, Float longitude, int rating, String comment) {
        LOG.debug("Add new rating to product:{}", productId);

        DRating dRating = null;
        int existing = -1;

        // specified users can only rate once
        DAppSettings settings = appSettingsDao.findByPrimaryKey(domain);
        boolean onlyRateOncePerUser = (null != settings) ? settings.getOnlyRateOncePerUser() : true;

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
            dProduct.setRatingSum((long)rating);
            dProduct.setRatingAverage(new Rating(rating));
        }
        else {
            // result exists, and existing rating for user
            if (-1 < existing) {
                dProduct.setRatingSum(dProduct.getRatingSum() - existing + rating);
                dProduct.setRatingAverage(new Rating((int)(dProduct.getRatingSum() / dProduct.getRatingCount())));
                // no need to update ratingCount!
            }
            else {
                // result exists, no existing rating for user
                dProduct.setRatingSum(dProduct.getRatingSum() + rating);
                dProduct.setRatingCount(dProduct.getRatingCount() + 1);
                dProduct.setRatingAverage(new Rating((int)(dProduct.getRatingSum() / dProduct.getRatingCount())));
            }
        }

        // Update product location if provided in the request
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            dProduct.setLocation(location);
        }

        // Persist and index location
        productDao.persistAndIndexLocation(dProduct);

        return dRating;
    }

    // Get a rating with a specific id
    public DRating getRating(long id) {
        LOG.debug("Get rating with id:{}", id);

        DRating dRating = ratingDao.findByPrimaryKey(id);

        if (null == dRating)
            throw new NotFoundException(404, String.format("Rating with id:%s not found", id));

        return dRating;
    }

    // Delete a ratings with a specific id
    @Idempotent
    @Transactional
    public DRating deleteRating(long id) {
        LOG.debug("Delete rating with id:{}",  id);

        DRating dRating = ratingDao.findByPrimaryKey(id);
        if (null == dRating)
            throw new NotFoundException(404, String.format("Rating with id:%s not found", id));

        // Delete the rating
        ratingDao.delete(dRating);

        // Update the product
        DProduct dProduct = productDao.findByPrimaryKey(dRating.getProductId());
        if (null != dProduct) {
            dProduct.setRatingSum(dProduct.getRatingSum() - dRating.getRating().getRating());
            dProduct.setRatingCount(dProduct.getRatingCount() - 1);

            // Guard against deleting the last rating to avoid / by zero
            if (0 != dProduct.getRatingCount())
                dProduct.setRatingAverage(new Rating((int)(dProduct.getRatingSum() / dProduct.getRatingCount())));
            else
                dProduct.setRatingAverage(null);

            productDao.persist(dProduct);
        } else
            // Should not happen, log error
            LOG.error("Rating exist but not the product:{}",  dRating.getProductId());

        return dRating;
    }

    // Get all ratings done by a specific user
    public Collection<DRating> getMyRatings(String username) {
        LOG.debug("Get all ratings for user:{}",  username);

        final Collection<DRating> dRatings = ratingDao.findByUsername(username);

        return dRatings;
    }

    // Create a histogram
    public Map<Long, Long> getHistogramForProduct(String productId, int interval) {
        LOG.debug("Create histogram for product:{}", productId);

        Collection<DRating> ratings = getAllRatingsForProduct(productId);

        Map<Long, Long> histogram = new HashMap<Long, Long>();

        for (DRating dRating : ratings) {
            // Find the interval
            long rounded = Math.round((float)dRating.getRating().getRating() / interval) * interval;
            Long currentFrequency = histogram.get(new Long(rounded));
            if (null == currentFrequency)
                // No previous value
                histogram.put(new Long(rounded), new Long(1));
            else
                // Update frequency
                histogram.put(new Long(rounded), new Long(currentFrequency.longValue() + 1));
        }

        return histogram;
    }

    /* Comment related methods */


    // Add a comment to a product
    @Idempotent
    @Transactional
    public DComment addComment(String productId, Long parentId, String username, Float latitude, Float longitude, String comment) {
        LOG.debug("Add new comment to product:(}",  productId);

        // If we have a parent comment id, check that it belongs to the same product if
        DComment dParentComment = getComment(parentId);
        if (null == dParentComment || dParentComment.getProductId().equalsIgnoreCase(productId) == false) {
            LOG.debug("Parent comment:{} is not a comment for the product:{}", parentId, productId);
            throw new BadRequestException(400, String.format("The parent comment:%s is not a comment for the product:%s", parentId, productId));
        }

        // Create new comment. Do not check if user have commented before
        DComment dComment = new DComment();
        dComment.setProductId(productId);
        if (null != parentId)
            dComment.setParentKey(commentDao.createKey(parentId));
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
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            dProduct.setLocation(location);
        }

        // Persist and index on location
        productDao.persistAndIndexLocation(dProduct);

        return dComment;
    }

    // Get a comment with a specific id
    public DComment getComment(long id) {
        LOG.debug("Get comment with id:{}", id);

        DComment dComment = commentDao.findByPrimaryKey(id);

        if (null == dComment)
            throw new NotFoundException(404, String.format("Comment with id:%s not found", id));

        return dComment;
    }

    // Delete a comment with a specific id
    @Idempotent
    @Transactional
    public DComment deleteComment(long id) {
        LOG.debug("Delete comment with id:{}", id);

        DComment dComment = commentDao.findByPrimaryKey(id);
        if (null == dComment)
            throw new NotFoundException(404, String.format("Comment with id:%s not found", id));

        // Delete the comment
        commentDao.delete(dComment);

        // Update the product
        DProduct dProduct = productDao.findByPrimaryKey(dComment.getProductId());
        if (null != dProduct) {
            dProduct.setCommentCount(dProduct.getCommentCount() - 1);
            productDao.persist(dProduct);
        } else
            // Should not happen, log error
            LOG.error("Comment exist but not the product:{}", dComment.getProductId());

        return dComment;
    }

    // Get all comments done by a specific user
    public Collection<DComment> getMyComments(String username) {
        LOG.debug("Get all comments for user:{}", username);

        final Collection<DComment> dComments = commentDao.findByUsername(username);

        return dComments;
    }

    /* Favorite related methods */


    // Add new favorite product
    @Idempotent
    @Transactional
    public DFavorites addFavorite(String productId, String username) {
        LOG.debug("Add product:{} as favorites for user:{}", productId, username);

        DFavorites dFavorites = favoritesDao.findByPrimaryKey(username);
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
        favoritesDao.persist(dFavorites);

        return dFavorites;
    }

    // Delete a product from favorites
    @Idempotent
    @Transactional
    public DFavorites deleteFavorite(String productId, String username) {
        LOG.debug("Delete product:{} from favorites for user:{}", productId, username);

        DFavorites dFavorites = favoritesDao.findByPrimaryKey(username);

        // If the favorite is not found return 404
        if (null == dFavorites || dFavorites.getProductIds().remove(productId) == false)
            throw new NotFoundException(404, String.format("Product:%s not found for user:%s", productId, username));

        // Store
        favoritesDao.persist(dFavorites);

        return dFavorites;
    }

    // Get all user favorites
    public DFavorites getFavorites(String username) {
        LOG.debug("Get favorites for user:{}", username);

        DFavorites dFavorites = favoritesDao.findByPrimaryKey(username);

        if (null == dFavorites)
            throw new NotFoundException(404, String.format("No favorites found for user:%s", username));

        return dFavorites;
    }


    /* Product related methods */

    // Get a specific product
    public DProduct getProduct(String productId) {
        LOG.debug("Get product:{}",  productId);

        final DProduct dProduct = productDao.findByPrimaryKey(productId);

        if (null == dProduct)
            throw new NotFoundException(404, String.format("Product with id:%s not found", productId));

        return dProduct;
    }

    // Get a list of products
    public Collection<DProduct> getProducts(String[] ids) {
        LOG.debug("Get a list of products:{}", ids);

        Map<String, DProduct> dProducts = productDao.findByPrimaryKeys(Arrays.asList(ids));

        return dProducts.values();
    }

    // Get all products
    public String getProductPage(String cursor, int pageSize, Collection<DProduct> resultList) {
        LOG.debug("Get product page with cursor:{} page size:{}", cursor, pageSize);
        return productDao.getProductPage(cursor, pageSize, resultList);
    }

    // Find nearby products with different sort order
    public String findNearbyProducts(String cursor, int pageSize, Float latitude, Float longitude, int radius, int sortOrder, Collection<DProduct> results) {
        LOG.debug("Find nearby products");

        DProductDao.SortOrder order;
        switch (sortOrder) {
            case 1:
                order = DProductDao.SortOrder.TOP_RATED;
                break;
            case 2:
                order = DProductDao.SortOrder.MOST_LIKED;
                break;
            default:
                order = DProductDao.SortOrder.DISTANCE;
                break;
        }

        String newCursor = productDao.searchInIndexForNearby(cursor, pageSize, latitude, longitude, radius, order, results);

        return newCursor;
    }

    // Find nearby products and return in KML format
    public String findNearbyProductsKml(String cursor, int pageSize, Float latitude, Float longitude, int radius, int sortOrder, PrintWriter out) {
        LOG.debug("Find nearby products. Return KML format");

        Collection<DProduct> dProducts = new ArrayList<DProduct>(pageSize);
        String newCursor = findNearbyProducts(cursor, pageSize, latitude, longitude, radius, sortOrder, dProducts);
        writeRatingsKml(out, dProducts);

        return newCursor;
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
            kmlDest.println("      <name>" + product.getProductId() + "</name>");
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
        LOG.debug("Get most liked products with limit:{}", limit);

        Collection<DProduct> dProducts = productDao.findMostLiked(limit);

        return dProducts;
    }

    // Get most thumbs up products
    public Collection<DProduct> getMostThumbsUpProducts(int limit) {
        LOG.debug("Get most thumbs up products with limit:{}", limit);

        Collection<DProduct> dProducts = productDao.findMostThumbsUp(limit);

        return dProducts;
    }

    // Get most thumbs down products
    public Collection<DProduct> getMostThumbsDownProducts(int limit) {
        LOG.debug("Get most thumbs down products with limit:{}", limit);

        Collection<DProduct> dProducts = productDao.findMostThumbsDown(limit);

        return dProducts;
    }


    // Get the most rated products
    public Collection<DProduct> getMostRatedProducts(int limit) {
        LOG.debug("Get most rated products with limit:{}", limit);

        Collection<DProduct> dProducts = productDao.findMostRated(limit);

        return dProducts;
    }

    // Get the top rated products
    public Collection<DProduct> getTopRatedProducts(int limit) {
        LOG.debug("Get top rated products with limit:{}", limit);

        Collection<DProduct> dProducts = productDao.findTopRated(limit);

        return dProducts;
    }

    // Get most commented products
    public Collection<DProduct> getMostCommentedProducts(int limit) {
        LOG.debug("Get most commented products with limit:{}", limit);

        Collection<DProduct> dProducts = productDao.findMostCommented(limit);

        return dProducts;
    }

     // Get all likes for a product
    public Collection<DLike> getAllLikesForProduct(String productId) {
        LOG.debug("Get all likes for product:{}", productId);

        Collection<DLike> dLikes = likeDao.findByProductId(productId);

        return dLikes;
    }

    // Get all thumbs for a product
    public Collection<DThumbs> getAllThumbsForProduct(String productId) {
        LOG.debug("Get all thumbs for product:{}", productId);

        Collection<DThumbs> dThumbs = thumbsDao.findByProductId(productId);

        return dThumbs;
    }

    // Get all ratings for a product
    public Collection<DRating> getAllRatingsForProduct(String productId) {
        LOG.debug("Get all ratings for product:{}", productId);

        Collection<DRating> dRatings = ratingDao.findByProductId(productId);

        return dRatings;
    }

    // Get all comments for a product
    public Collection<DComment> getAllCommentsForProduct(String productId) {
        LOG.debug("Get all comments for product:{}", productId);

        Collection<DComment> dComments = commentDao.findByProductId(productId);

        return dComments;
    }

    // Get all products a user have liked
    public Collection<DProduct> getProductsLikedByUser(String username) {
        LOG.debug("Get all products liked by user:{}", username);

        final Collection<DLike> myLikes = likeDao.findByUsername(username);

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>(myLikes.size());
        for (DLike like : myLikes)
            productIds.add(like.getProductId());

        final Map<String, DProduct> dProducts = productDao.findByPrimaryKeys(productIds);

        return dProducts.values();
    }

    // Get all products a user have thumbed
    public Collection<DProduct> getProductsThumbedByUser(String username) {
        LOG.debug("Get all products thumbed by user:{}", username);

        final Collection<DThumbs> myThumbs = thumbsDao.findByUsername(username);

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>(myThumbs.size());
        for (DThumbs dThumbs : myThumbs)
            productIds.add(dThumbs.getProductId());

        final Map<String, DProduct> dProducts = productDao.findByPrimaryKeys(productIds);

        return dProducts.values();
    }

    // Get all products a user have rated
    public Collection<DProduct> getProductsRatedByUser(String username) {
        LOG.debug("Get all products rated by user:{}", username);

        final Collection<DRating> myRatings = ratingDao.findByUsername(username);

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>(myRatings.size());
        for (DRating rating : myRatings)
            productIds.add(rating.getProductId());

        final Map<String, DProduct> dProducts = productDao.findByPrimaryKeys(productIds);

        return dProducts.values();
    }

    // Get all products a user have commented
    public Collection<DProduct> getProductsCommentedByUser(String username) {
        LOG.debug("Get all products commented by user:{}", username);

        final Collection<DComment> myComments = commentDao.findByUsername(username);

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>(myComments.size());
        for (DComment dComment : myComments)
            productIds.add(dComment.getProductId());

        final Map<String, DProduct> dProducts = productDao.findByPrimaryKeys(productIds);

        return dProducts.values();
    }


    // Get all users favorite products
    public Collection<DProduct> geUserFavoriteProducts(String username) {
        LOG.debug("Get favorite products for user:{}", username);

        final DFavorites dFavorites =favoritesDao.findByPrimaryKey(username);

        final Map<String, DProduct> dProducts = productDao.findByPrimaryKeys(dFavorites.getProductIds());

        return  dProducts.values();
    }


    // Setters and getters
    public void setAppDao(DAppDao appDao) {
        this.appDao = appDao;
    }

    public void setRatingDao(DRatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public void setProductDao(DProductDao productDao) {
        this.productDao = productDao;
    }

    public void setLikeDao(DLikeDao likeDao) {
        this.likeDao = likeDao;
    }

    public void setThumbsDao(DThumbsDao thumbsDao) {
        this.thumbsDao = thumbsDao;
    }

    public void setCommentDao(DCommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public void setFavoritesDao(DFavoritesDao favoritesDao) {
        this.favoritesDao = favoritesDao;
    }

    public void setAppSettingsDao(DAppSettingsDao appSettingsDao) {
        this.appSettingsDao = appSettingsDao;
    }
}
