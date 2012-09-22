package com.wadpam.rnr.domain;

import net.sf.mardao.api.domain.AEDStringEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * This class contain settings that can be done per application
 * @author mattiaslevin
 */
@Entity
public class DAppSettings extends AEDStringEntity implements Serializable {

    private static final long serialVersionUID = 2237539236676704660L;


    /** The unique domain name for the app */
    @Id
    private String      domainName;

    /** Decides if an identified user only can like a product once  */
    @Basic
    private Boolean     onlyLikeOncePerUser = true;

    /** Decides if an identified user only can rate a product once  */
    @Basic
    private Boolean     onlyRateOncePerUser = true;


    @Override
    public String getSimpleKey() {
        return getDomainName();
    }

    @Override
    public String toString() {
        return String.format("{domain:%s, only like once:%d only rate once:%d}",
                getDomainName(), getOnlyLikeOncePerUser(), getOnlyRateOncePerUser());
    }


    // Setters and getters
    public Boolean getOnlyLikeOncePerUser() {
        return onlyLikeOncePerUser;
    }

    public void setOnlyLikeOncePerUser(Boolean onlyLikeOncePerUser) {
        this.onlyLikeOncePerUser = onlyLikeOncePerUser;
    }

    public Boolean getOnlyRateOncePerUser() {
        return onlyRateOncePerUser;
    }

    public void setOnlyRateOncePerUser(Boolean onlyRateOncePerUser) {
        this.onlyRateOncePerUser = onlyRateOncePerUser;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
