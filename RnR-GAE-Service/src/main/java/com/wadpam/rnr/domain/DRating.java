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
import java.util.Collection;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import net.sf.mardao.api.geo.aed.GeoModel;

/**
 *
 * @author os
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"productId", "username"})})
public class DRating extends AEDLongEntity implements GeoModel {

    /** Generated primary key */
    @Id
    private Long               id;

    /** The Many-To-One productId (unconstrained) */
    @Basic
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    @Basic
    private String             username;

    /** The location of the product */
    // TODO: The location should be move to the DResult domain object
    @Basic
    private GeoPt location;

    /** A user-provided integer rating for a piece of content. Normalized to a 0-100 scale. */
    @Basic
    private Rating rating;

    @Basic
    /** used by GeoDao to index boxes */
    private Collection<Long> geoboxes;

    @Override
    public Long getSimpleKey() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s, location:%s, rating:%s}",
                id, productId, username, location, rating);
    }

    // Needed for geoloaction
    @Override
    public float getLatitude() {
        return null != location ? location.getLatitude() : 0;
    }

    @Override
    public float getLongitude() {
        return null != location ? location.getLongitude() : 0;
    }

    @Override
    public void setGeoboxes(Collection<Long> geoboxes) {
        this.geoboxes = geoboxes;
    }

    public Collection<Long> getGeoboxes() {
        return geoboxes;
    }

    // Setters and getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GeoPt getLocation() {
        return location;
    }

    public void setLocation(GeoPt location) {
        this.location = location;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

}
