package com.wadpam.rnr.json;

import java.util.Collection;
import java.util.Set;

/**
 * Json object for favorites.
 * @author mlv
 */
public class JFavorites extends JBaseObject {

    /** The user name */
    private String                  username;

    /** The users favorite products ids */
    private Collection<String>      productIds;


    @Override
    protected String subString() {
        return String.format("username:%s, favorites:%s", getUsername(), getProductIds());
    }


    // Setters and Getters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(Collection<String> productIds) {
        this.productIds = productIds;
    }
}
