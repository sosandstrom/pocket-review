package com.wadpam.rnr.domain;

import net.sf.mardao.core.domain.AbstractCreatedUpdatedEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;

/**
 * The domain object for Favorites
 * @author mattiaslevin.
 */
@Entity
public class DFavorites extends AbstractCreatedUpdatedEntity implements Serializable {

    private static final long serialVersionUID = 6873407503201116079L;

    /** The user name */
    @Id
    private String                 username;

    @Basic
    /** The users favorite products ids */
    private Collection<String>     productIds;


    @Override
    public String toString() {
        return String.format("{username:%s, favorite products:%s}", username, productIds);
    }


    // Getter and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(Collection<String> productIds) {
        this.productIds = productIds;
    }
}
