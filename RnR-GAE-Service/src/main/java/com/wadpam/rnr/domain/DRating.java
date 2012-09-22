/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.domain;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Category;
import net.sf.mardao.api.domain.AEDLongEntity;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Rating;
import java.util.Collection;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import net.sf.mardao.api.geo.aed.GeoModel;

/**
 * The domain object for Ratings.
 * @author os
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"productId", "username", "category"})})
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

    /** A user-provided integer rating for a piece of content. Normalized to a 0-100 scale. */
    @Basic
    private Rating             rating;

    /** A user-provided review comment */
    @Basic
    private String             comment;

    /** The category of the rating if supporting group ratings */
    @Basic
    private Category           category;


    @Override
    public Long getSimpleKey() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s, rating:%s, comment:%s}",
                id, productId, username, rating, comment);
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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
