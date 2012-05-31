/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.domain;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

import net.sf.mardao.api.domain.AEDLongEntity;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Rating;

/**
 *
 * @author os
 */
@Entity
public class DRating extends AEDLongEntity {
    /** Generated primary key */
    @Id
    private Long               id;

    /** The Many-To-One productId (unconstrained) */
    @Basic
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    @Basic
    private String             username;

    /** Where was this product rated */
    @Basic
    private GeoPt location;

    /** A user-provided integer rating for a piece of content. Normalized to a 0-100 scale. */
    @Basic
    private Rating rating;

    @Override
    public Long getSimpleKey() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s, location:%s, rating:%s}",
                id, productId, username, location, rating);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeoPt getLocation() {
        return location;
    }

    public void setLocation(GeoPt location) {
        this.location = location;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
}
