/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.domain;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;


import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Rating;
import net.sf.mardao.api.domain.AEDStringEntity;
import net.sf.mardao.api.geo.aed.GeoModel;

import java.util.Collection;

/**
 *
 * @author os
 */
@Entity
public class DResult extends AEDStringEntity {

    /** The Many-To-One productId (unconstrained) */
    @Id
    private String      productId;

    /** The total sum or all ratings */
    @Basic
    private Long        ratingSum = 0L;

    /** The total number of ratings */
    @Basic
    private Long        ratingCount = 0L;

    /** The total number of likes */
    @Basic
    private Long        numberOfLikes = 0L;

    // TODO: Move location to this place

    @Override
    public String getSimpleKey() {
        return productId;
    }

    @Override
    public String toString() {
        return String.format("{productId:%s, ratings:%d, average:%d}",
                productId, ratingCount, getAverage());
    }

    // Getters and setters
    public int getAverage() {
        return 0 < ratingCount ? (int) (ratingSum / ratingCount) : -1;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public Long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }
}
