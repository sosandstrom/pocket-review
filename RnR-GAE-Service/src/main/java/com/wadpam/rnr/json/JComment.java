package com.wadpam.rnr.json;

/**
 * Json object for comments.
 * @author mlv
 */
public class JComment extends JBaseObject {

    /**  The unique id of the Comment */
    private String             id;

    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** The location of the product */
    private JLocation          location;

    /** A user-provided comment */
    private String             comment;


    @Override
    protected String subString() {
        return String.format("id:%s productId:%s, username:%s, location:%s, comment:%s",
                id, productId, username, location, comment);
    }

    // Setters and Getters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

