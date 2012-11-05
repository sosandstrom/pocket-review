package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.*;
import com.wadpam.open.transaction.Idempotent;
import com.wadpam.rnr.dao.*;
import com.wadpam.rnr.domain.*;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

import com.wadpam.server.exceptions.BadRequestException;
import com.wadpam.server.exceptions.NotFoundException;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author os
 */
public class RnrService {

    static final Logger LOG = LoggerFactory.getLogger(RnrService.class);

    private DAppSettingsDao appSettingsDao;
    private DProductDao productDao;
    private DRatingDao ratingDao;
    private DLikeDao likeDao;
    private DThumbsDao thumbsDao;
    private DCommentDao commentDao;
    private DFavoritesDao favoritesDao;

    // Decide the sort order in nearby searches
    public enum SortOrder {DISTANCE, TOP_RATED, MOST_LIKED, MOST_THUMBS_UP}

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


    // Init
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
            //dLike.setCreatedBy(username);
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
        productDao.persist(dProduct);

        // Call to generate a datastrore ConcurrentModificationException in order to test the transaction retry mechanism
        // Uncomment this to run tests
        //throw(new ConcurrentModificationException());

        return dLike;
    }

    // Get a like with a specific id
    public DLike getLike(long id) {
        LOG.debug("Get like with id:{}", id);
        return likeDao.findByPrimaryKey(id);
    }


    // Delete a like with a specific id
    @Idempotent
    @Transactional
    public DLike deleteLike(long id) {
        LOG.debug("Delete like with id:{}", id);

        DLike dLike = likeDao.findByPrimaryKey(id);
        if (null == dLike)
           return null;

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
    public Iterable<DLike> getMyLikes(String username) {
        LOG.debug("Get all likes for user:{}", username);
        return likeDao.queryByUsername(username);
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
        productDao.persist(dProduct);

        return dThumbs;
    }

    // Delete a specific thumbs
    @Idempotent
    @Transactional
    public DThumbs deleteThumbs(long id) {
        LOG.debug("Delete thumb with id:{}", id);

        DThumbs dThumbs = thumbsDao.findByPrimaryKey(id);
        if (null == dThumbs)
            return null;

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
        return thumbsDao.findByPrimaryKey(id);
    }

    // Get my thumbs for user
    public Iterable<DThumbs> getMyThumbs(String username) {
        LOG.debug("Get all thumbs for user:{}", username);
        return thumbsDao.queryByUsername(username);
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
        productDao.persist(dProduct);

        return dRating;
    }

    // Get a rating with a specific id
    public DRating getRating(long id) {
        LOG.debug("Get rating with id:{}", id);
        return ratingDao.findByPrimaryKey(id);
    }

    // Delete a ratings with a specific id
    @Idempotent
    @Transactional
    public DRating deleteRating(long id) {
        LOG.debug("Delete rating with id:{}",  id);

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
    public Iterable<DRating> getMyRatings(String username) {
        LOG.debug("Get all ratings for user:{}",  username);
        return ratingDao.queryByUsername(username);
    }

    // Create a histogram
    public Map<Long, Long> getHistogramForProduct(String productId, int interval) {
        LOG.debug("Create histogram for product:{}", productId);

        Iterator<DRating> dRatingIterator = ratingDao.queryByProductId(productId).iterator();

        // Build histogram
        Map<Long, Long> histogram = new HashMap<Long, Long>();
        while (dRatingIterator.hasNext() ) {
            DRating dRating = dRatingIterator.next();

            // Find the interval
            long rounded = Math.round((float)dRating.getRating().getRating() / interval) * interval;

            Long currentFrequency = histogram.get(rounded);
            if (null == currentFrequency)
                // No previous value
                histogram.put(rounded, 1L);
            else
                // Update frequency
                histogram.put(rounded, currentFrequency + 1);
        }

        return histogram;
    }

    /* Comment related methods */


    // Add a comment to a product
    @Idempotent
    @Transactional
    public DComment addComment(String productId, String username, Float latitude, Float longitude, String comment) {
        LOG.debug("Add new comment to product:(}",  productId);

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
        if (null != latitude && null != longitude) {
            final GeoPt location = new GeoPt(latitude, longitude);
            dProduct.setLocation(location);
        }

        // Persist and index on location
        productDao.persist(dProduct);

        return dComment;
    }

    // Get a comment with a specific id
    public DComment getComment(long id) {
        LOG.debug("Get comment with id:{}", id);
        return commentDao.findByPrimaryKey(id);
    }

    // Delete a comment with a specific id
    @Idempotent
    @Transactional
    public DComment deleteComment(long id) {
        LOG.debug("Delete comment with id:{}", id);

        DComment dComment = commentDao.findByPrimaryKey(id);
        if (null == dComment)
            return null;

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
    public Iterable<DComment> getMyComments(String username) {
        LOG.debug("Get all comments for user:{}", username);
        return commentDao.queryByUsername(username);
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
            return null;

        // Store
        favoritesDao.persist(dFavorites);

        return dFavorites;
    }

    // Get all user favorites
    public DFavorites getFavorites(String username) {
        LOG.debug("Get favorites for user:{}", username);
        return favoritesDao.findByPrimaryKey(username);
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
    public Iterable<DProduct> getProducts(String[] ids) {
        LOG.debug("Get a list of products:{}", ids);
        return productDao.queryByPrimaryKeys(null, Arrays.asList(ids));
    }

    // Get all products
    public CursorPage<DProduct, String> getProductPage(int pageSize, String cursor) {
        LOG.debug("Get product page with cursor:{} page size:{}", cursor, pageSize);
        return productDao.queryPage(pageSize, cursor);
    }

    // Find nearby products with different sort order
    public CursorPage<DProduct, String> findNearbyProducts(int pageSize, String cursor, Float latitude, Float longitude,
                                                           int radius, SortOrder sort) {
        LOG.debug("Find nearby products");
        return productDao.searchForNearby(pageSize, cursor, latitude, longitude, radius, sort);
    }

    // Get the most liked products
    public CursorPage<DProduct, String> getMostLikedProducts(int limit, Serializable cursor) {
        LOG.debug("Get most liked products with limit:{}", limit);
        return productDao.queryMostLiked(limit, cursor);
    }

    // Get most thumbs up products
    public CursorPage<DProduct, String> getMostThumbsUpProducts(int limit, Serializable cursor) {
        LOG.debug("Get most thumbs up products with limit:{}", limit);
        return productDao.queryMostThumbsUp(limit, cursor);
    }

    // Get most thumbs down products
    public CursorPage<DProduct, String> getMostThumbsDownProducts(int limit, Serializable cursor) {
        LOG.debug("Get most thumbs down products with limit:{}", limit);
        return productDao.queryMostThumbsDown(limit, cursor);
    }

    // Get the most rated products
    public CursorPage<DProduct, String> getMostRatedProducts(int limit, Serializable cursor) {
        LOG.debug("Get most rated products with limit:{}", limit);
        return productDao.queryMostRated(limit, cursor);
    }

    // Get the top rated products
    public CursorPage<DProduct, String> getTopRatedProducts(int limit, Serializable cursor) {
        LOG.debug("Get top rated products with limit:{}", limit);
        return productDao.queryTopRated(limit, cursor);
    }

    // Get most commented products
    public CursorPage<DProduct, String> getMostCommentedProducts(int limit, Serializable cursor) {
        LOG.debug("Get most commented products with limit:{}", limit);
        return productDao.queryMostCommented(limit, cursor);
    }

     // Get all likes for a product
    public CursorPage<DLike, Long> getAllLikesForProduct(String productId, int limit, Serializable cursor) {
        LOG.debug("Get all likes for product:{}", productId);
        return likeDao.queryPageByProductId(productId, limit, cursor);
    }

    // Get all thumbs for a product
    public CursorPage<DThumbs, Long> getAllThumbsForProduct(String productId, int limit, Serializable cursor) {
        LOG.debug("Get all thumbs for product:{}", productId);
        return thumbsDao.queryPageByProductId(productId, limit, cursor);
    }

    // Get all ratings for a product
    public CursorPage<DRating, Long> getAllRatingsForProduct(String productId, int limit, Serializable cursor) {
        LOG.debug("Get all ratings for product:{}", productId);
        return ratingDao.queryPageByProductId(productId, limit, cursor);
    }

    // Get all comments for a product
    public CursorPage<DComment, Long> getAllCommentsForProduct(String productId, int limit, Serializable cursor) {
        LOG.debug("Get all comments for product:{}", productId);
        return commentDao.queryPageByProductId(productId, limit, cursor);
    }

    // Get all products a user have liked
    public Iterable<DProduct> getProductsLikedByUser(String username) {
        LOG.debug("Get all products liked by user:{}", username);

        final Iterator<DLike> dLikeIterator = likeDao.queryByUsername(username).iterator();

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>();
        while (dLikeIterator.hasNext())
            productIds.add(dLikeIterator.next().getProductId());

        return productDao.queryByPrimaryKeys(null, productIds);
    }

    // Get all products a user have thumbed
    public Iterable<DProduct> getProductsThumbedByUser(String username) {
        LOG.debug("Get all products thumbed by user:{}", username);

        final Iterator<DThumbs> dThumbsIterator = thumbsDao.queryByUsername(username).iterator();

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>();
        while (dThumbsIterator.hasNext())
            productIds.add(dThumbsIterator.next().getProductId());

        return productDao.queryByPrimaryKeys(null, productIds);
    }

    // Get all products a user have rated
    public Iterable<DProduct> getProductsRatedByUser(String username) {
        LOG.debug("Get all products rated by user:{}", username);

        final Iterator<DRating> dRatingIterator = ratingDao.queryByUsername(username).iterator();

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>();
        while (dRatingIterator.hasNext())
            productIds.add(dRatingIterator.next().getProductId());

        return productDao.queryByPrimaryKeys(null, productIds);
    }

    // Get all products a user have commented
    public Iterable<DProduct> getProductsCommentedByUser(String username) {
        LOG.debug("Get all products commented by user:{}", username);

        final Iterator<DComment> dCommentIterator = commentDao.queryByUsername(username).iterator();

        // Collect all unique product ids and get their products
        Set<String> productIds = new HashSet<String>();
        while (dCommentIterator.hasNext())
            productIds.add(dCommentIterator.next().getProductId());

        return productDao.queryByPrimaryKeys(null, productIds);
    }


    // Get all users favorite products
    public Iterable<DProduct> geUserFavoriteProducts(String username) {
        LOG.debug("Get favorite products for user:{}", username);

        final DFavorites dFavorites = favoritesDao.findByPrimaryKey(username);

        return productDao.queryByPrimaryKeys(null, dFavorites.getProductIds());
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
