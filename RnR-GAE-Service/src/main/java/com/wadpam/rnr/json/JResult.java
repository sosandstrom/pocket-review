package com.wadpam.rnr.json;

/**
 *
 * @author os
 */
public class JResult extends JBaseObject {

    /** The Many-To-One productId (unconstrained) */
    private String      productId;

    /** The location of the product */
    // TODO: Never populated
    private JLocation   location;

    /** The total sum or all ratings */
    private Long        ratingSum = 0L;

    /** The total number of ratings */
    private Long        ratingCount = 0L;

    /** The total number of likes */
    private Long        numberOfLikes = 0L;


    @Override
    protected String subString() {
        return String.format("productId:%s, location:%s, ratings:%d, average:%d, likes:%d",
                productId, location, ratingCount, getAverage(), getNumberOfLikes());
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public int getAverage() {
        return 0 < ratingCount ? (int) (ratingSum / ratingCount) : 0;
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

    public Long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }
}
