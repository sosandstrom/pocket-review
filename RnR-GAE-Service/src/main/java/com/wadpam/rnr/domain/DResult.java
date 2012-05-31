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

/**
 *
 * @author os
 */
@Entity
public class DResult extends AEDStringEntity {
    @Id
    private String               productId;

    @Basic
    private Long ratingSum = 0L;

    @Basic
    private Long ratingCount = 0L;

    @Override
    public String getSimpleKey() {
        return productId;
    }

    @Override
    public String toString() {
        return String.format("{productId:%s, ratings:%d, average:%d}",
                productId, ratingCount, getAverage());
    }
    
    public int getAverage() {
        return 0 < ratingCount ? (int) (ratingSum / ratingCount) : -1;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
