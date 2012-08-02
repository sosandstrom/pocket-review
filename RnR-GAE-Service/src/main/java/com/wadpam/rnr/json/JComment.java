package com.wadpam.rnr.json;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/27/12
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
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

