package com.wadpam.rnr.domain;


import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Rating;
import net.sf.mardao.core.domain.AbstractCreatedUpdatedEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;

/**
 * The domain object for Products.
 * @author os
 */
@Entity
public class DProduct extends AbstractCreatedUpdatedEntity implements Serializable {

    private static final long serialVersionUID = -2815964311490330958L;


    /** The Many-To-One productId (unconstrained) */
    @Id
    private String      productId;

    /** The category of the rating if supporting group ratings */
    @Basic
    private Category    category;

    /** The total sum or all ratings */
    @Basic
    private Long        ratingSum = 0L;

    /** The total number of ratings */
    @Basic
    private Long        ratingCount = 0L;

    /** The calculated average rating. Normalize to a 0-100 scale. */
    @Basic
    private Rating      ratingAverage;

    /** The total number of likes */
    @Basic
    private Long        likeCount = 0L;

    /** Contain a list of 10 random user names that liked this product */
    @Basic
    private Collection<String> likeRandomUsernames;

    /**
     * If the current user liked this product.
     * Not stored in DB, calculated dynamically.
     */
    private Boolean     likedByUser;

    /** The number of thumbs up */
    @Basic
    private Long        thumbsUp = 0L;

    /** The number of thumbs up */
    @Basic
    private Long        thumbsDown = 0L;

    /** The total number of comments */
    @Basic
    private Long        commentCount = 0L;

    /** The location of the product */
    @Basic
    private GeoPt       location;


    @Override
    public String toString() {
        return String.format("{productId:%s, ratings:%d, average:%s, likes:%d, location:%s}",
                productId, ratingCount, ratingAverage, likeCount, location);
    }


    public float getLatitude() {
        return null != location ? location.getLatitude() : -200;
    }

    public float getLongitude() {
        return null != location ? location.getLongitude() : -200;
    }


    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(Long ratingSum) {
        this.ratingSum = ratingSum;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Rating getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Rating ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public GeoPt getLocation() {
        return location;
    }

    public void setLocation(GeoPt location) {
        this.location = location;
    }

    public Long getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(Long thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public Long getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(Long thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public Collection<String> getLikeRandomUsernames() {
        return likeRandomUsernames;
    }

    public void setLikeRandomUsernames(Collection<String> likeRandomUsernames) {
        this.likeRandomUsernames = likeRandomUsernames;
    }

    public Boolean getLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(Boolean likedByUser) {
        this.likedByUser = likedByUser;
    }
}
