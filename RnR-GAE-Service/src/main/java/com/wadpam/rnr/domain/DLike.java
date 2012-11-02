package com.wadpam.rnr.domain;

import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * The domain object for Likes.
 * @author mattiaslevin
 */
@Entity
public class DLike extends AbstractLongEntity {

    /** The Many-To-One productId (unconstrained) */
    @Basic
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    @Basic
    private String             username;


    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s}", getId(), productId, username);
    }

    // Setters and Getters
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

}
