package com.wadpam.rnr.json;

/**
 *
 * @author os
 */
public class JResult extends JBaseObject {

    private Long ratingSum = 0L;

    private Long ratingCount = 0L;

    @Override
    protected String subString() {
        return String.format("ratings:%d, average:%d",
                ratingCount, getAverage());
    }
    
    public int getAverage() {
        return 0 < ratingCount ? (int) (ratingSum / ratingCount) : -1;
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

}
