package com.wadpam.rnr.domain;

import net.sf.mardao.api.domain.AEDStringEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 8/3/12
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class DFavorites extends AEDStringEntity  {

    /** The user name */
    @Id
    private String                username;

    @Basic
    /** The users favorite products ids */
    private Collection<String>     productIds;


    @Override
    public String getSimpleKey() {
        return getUsername();
    }

    @Override
    public String toString() {
        return String.format("{username:%s, favorite products:%s}", getUsername(), getProductIds());
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
