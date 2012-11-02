package com.wadpam.rnr.domain;

import com.google.appengine.api.datastore.Key;
import net.sf.mardao.api.Parent;
import net.sf.mardao.api.domain.AEDLongEntity;
import net.sf.mardao.core.domain.AbstractCreatedUpdatedEntity;
import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.*;

/**
 * The domain object for Comments.
 * @author mattiaslevin
 */
@Entity
public class DComment extends AbstractLongEntity {


    /** The Many-To-One productId (unconstrained) */
    @Basic
    private String             productId;

    /** Parent id. Can be used when writing comments on comments or Question and Answers services */
    @Parent(kind = "DComment")
    private Key                parentKey;

    /** The Many-To-One username (unconstrained) */
    @Basic
    private String             username;

    /** A user-provided comment */
    @Basic
    private String             comment;


    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s, comment:%s}",
                getId(), productId, username, comment);
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Key getParentKey() {
        return parentKey;
    }

    public void setParentKey(Key parentKey) {
        this.parentKey = parentKey;
    }
}
