package com.wadpam.rnr.domain;

import net.sf.mardao.api.domain.AEDLongEntity;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/27/12
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"productId", "username"})})
public class DComment extends AEDLongEntity {

    /** Generated primary key */
    @Id
    private Long               id;

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
    public Long getSimpleKey() {
        return getId();
    }

    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, username:%s, comment:%s}",
                getId(), getProductId(), getUsername(), getComment());
    }


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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
