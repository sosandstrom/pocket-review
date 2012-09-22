package com.wadpam.rnr.json;

import com.wadpam.open.json.JBaseObject;
import com.wadpam.open.json.JLocation;

import javax.persistence.Basic;

/**
 * The Json object for products.
 * @author os
 */
public class JProduct extends JBaseObject {

    /** The location of the product */
    private JLocation   location;

    /**
     * The distance in km between the product location and the device provided position.
     * If either the device position or product location is unknown the distance will not be calculated.
     * This property will be set in nearby searches.
     */
    private Float       distance;

    /** The total sum or all ratings */
    private Long        ratingSum = 0L;

    /** The total number of ratings */
    private Long        ratingCount = 0L;

    /** The calculated average rating. Normalize to a 0-100 scale. */
    private Integer     ratingAverage = -1;

    /** The deep link to the individual ratings */
    private String      ratingsURL;

    /** The total number of Likes */
    private Long        likeCount = 0L;

    /** The deep link to the individual likes */
    private String      likesURL;

    /** The total number of Comments */
    private Long        commentCount = 0L;

    /** The deep link to the individual likes */
    private String      commentsURL;


    @Override
    protected String subString() {
        return String.format("location:%s, ratings:%d, average:%d, likes:%d, comments:%d",
                location, ratingCount, getRatingAverage(), likeCount, commentCount);
    }

    // Setters and Getters
    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Long getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(Long ratingSum) {
        this.ratingSum = ratingSum;
    }

    public Integer getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Integer ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public String getRatingsURL() {
        return ratingsURL;
    }

    public void setRatingsURL(String ratingsURL) {
        this.ratingsURL = ratingsURL;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikesURL() {
        return likesURL;
    }

    public void setLikesURL(String likesURL) {
        this.likesURL = likesURL;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentsURL() {
        return commentsURL;
    }

    public void setCommentsURL(String commentsURL) {
        this.commentsURL = commentsURL;
    }
}
