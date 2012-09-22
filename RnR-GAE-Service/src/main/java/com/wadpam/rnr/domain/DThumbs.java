package com.wadpam.rnr.domain;

import net.sf.mardao.api.domain.AEDLongEntity;

import javax.persistence.*;

/**
 * The domain object for Likes.
 * @author mattiaslevin
 */
@Entity
public class DThumbs extends AEDLongEntity {

    /** Generated primary key */
    @Id
    private Long               id;

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
    public Long getSimpleKey() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s value:%s}", id, productId, productId, value);
    }

    // Setters and getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
