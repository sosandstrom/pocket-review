/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.json;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Rating;
import com.wadpam.open.json.JBaseObject;
import com.wadpam.open.json.JLocation;

import javax.persistence.Basic;

/**
 * Json object for ratings.
 * @author mattiaslevin
 */
public class JRating extends JBaseObject {

    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** The location of the product */
    private JLocation          location;

    /** A user-provided integer rating for a piece of content. Normalized to a 0-100 scale. */
    private Integer            rating;

    /** A user-provided review comment */
    private String             comment;


    @Override
    protected String subString() {
        return String.format("productId:%s, username:%s, location:%s, rating:%d, comment:%s",
                productId, username, location, rating, comment);
    }


    // Setters and getters
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

    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
