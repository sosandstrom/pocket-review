package com.wadpam.rnr.json;

import com.wadpam.open.json.JBaseObject;
import com.wadpam.open.json.JLocation;

import java.util.Collection;

/**
 * Json object for comments.
 * @author mattiaslevin
 */
public class JComment extends JBaseObject {

    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** An optional parent */
    private Long               parentId;

    /** Child comments */
    private Collection<JComment>   children;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** The location of the product */
    private JLocation          location;

    /** A user-provided comment */
    private String             comment;


    @Override
    protected String subString() {
        return String.format("productId:%s, username:%s, location:%s, comment:%s",
                productId, username, location, comment);
    }

    // Setters and Getters
    public Collection<JComment> getChildren() {
        return children;
    }

    public void setChildren(Collection<JComment> children) {
        this.children = children;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

