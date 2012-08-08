package com.wadpam.rnr.domain;

import net.sf.mardao.api.domain.AEDStringEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * This class contain settings that can be done per application
 * @author mlv
 */
@Entity
public class DAppSettings extends AEDStringEntity implements Serializable {

    private static final long serialVersionUID = -2876261095753959546L;


    /** The unique domain name for the app */
    @Id
    private String      domainName; // Can not use the name domain, reserved name in String class?

    /** An unique appId generated when the app is created. Most be provided in the request from the apps */
    @Basic
    private String      appId;

    /** A generated app key. Must be provided in the requests from the apps */
    @Basic
    private String      appKey;

    /** Decides if an identified user only can like a product once  */
    @Basic
    private Boolean     onlyLikeOncePerUser;

    /** Decides if an identified user only can rate a product once  */
    @Basic
    private Boolean     onlyRateOncePerUser;

    // TODO: Is there anything else we like to control per app?

    @Override
    public String getSimpleKey() {
        return getDomainName();
    }

    @Override
    public String toString() {
        return String.format("{domain:%s, appId:%s, appKey:%s, likeOncePerUser:%s, rateOncePerUser:%s}",
                getDomainName(), getAppId(), getAppKey(), getOnlyLikeOncePerUser(), getOnlyRateOncePerUser());
    }


    // Getters and setters
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

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
}
