package com.wadpam.rnr.domain;

import net.sf.mardao.api.domain.AEDLongEntity;
import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.*;

/**
 * The domain object for Likes.
 * @author mattiaslevin
 */
@Entity
public class DThumbs extends AbstractLongEntity {

    /** The Many-To-One productId (unconstrained) */
    @Basic
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    @Basic
    private String             username;

    /** The Many-To-One username (unconstrained) */
    @Basic
    private Long               value;


    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s value:%s}", getId(), productId, productId, value);
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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
