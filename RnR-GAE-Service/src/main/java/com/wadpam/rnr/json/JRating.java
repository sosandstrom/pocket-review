/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.json;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Rating;
import javax.persistence.Basic;

/**
 *
 * @author os
 */
public class JRating extends JBaseObject {
    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** Where was this product rated */
    private JLocation location;

    /** A user-provided integer rating for a piece of content. Normalized to a 0-100 scale. */
    private Integer rating;

    public JRating() {
    }

    public JRating(String id, Long createdDate, Long updatedDate) {
        super(id, createdDate, updatedDate, updatedDate);
    }

    @Override
    protected String subString() {
        return String.format("productId:%s, username:%s, location:%s, rating:%s",
                productId, username, location, rating);
    }
    
    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
