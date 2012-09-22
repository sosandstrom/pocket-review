package com.wadpam.rnr.json;

import com.wadpam.open.json.JBaseObject;
import com.wadpam.open.json.JLocation;

/**
 * Json object for likes.
 * @author mattiaslevin
 */
public class JLike extends JBaseObject {


    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** The location of the product */
    private JLocation          location;


    @Override
    protected String subString() {
        return String.format("productId:%s, username:%s, location:%s",
                productId, username, location);
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
}
