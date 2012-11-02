package com.wadpam.rnr.domain;

import com.google.appengine.api.datastore.Key;
import net.sf.mardao.core.Parent;
import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * The domain object for Comments.
 * @author mattiaslevin
 */
@Entity
public class DComment extends AbstractLongEntity {


    /** The Many-To-One productId (unconstrained) */
    @Basic
    private String             productId;

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

}
