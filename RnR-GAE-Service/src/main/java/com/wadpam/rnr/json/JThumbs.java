package com.wadpam.rnr.json;

import com.wadpam.open.json.JBaseObject;
import com.wadpam.open.json.JLocation;

/**
 * Json object for thumbs up and down.
 * @author mattiaslevin
 */
public class JThumbs extends JBaseObject {

    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** The location of the product */
    private JLocation          location;

    /**
     * The value of the thumb.
     * +1 for thumbs up
     * -1 for thumbs down
     */
    private Integer             value;


    @Override
    protected String subString() {
        return String.format("productId:%s, username:%s, location:%s value:%d",
                productId, username, location, value);
    }


    // Setters and getters
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
