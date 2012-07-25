package com.wadpam.rnr.domain;

import com.google.appengine.api.datastore.GeoPt;
import net.sf.mardao.api.domain.AEDLongEntity;
import net.sf.mardao.api.geo.aed.GeoModel;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/24/12
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"productId", "username"})})
public class DLike extends AEDLongEntity {

    /** Generated primary key */
    @Id
    private Long               id;

    /** The Many-To-One productId (unconstrained) */
    @Basic
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    @Basic
    private String             username;

    @Override
    public Long getSimpleKey() {
        return getId();
    }

    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s}", getId(), getProductId(), getUsername());
    }

    // Setters and Getters
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

}
