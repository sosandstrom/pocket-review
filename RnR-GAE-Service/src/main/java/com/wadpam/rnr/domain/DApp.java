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
public class DApp extends AEDStringEntity implements Serializable {

    private static final long serialVersionUID = -5390529587908700014L;

    /** The unique domain name for the app */
    @Id
    private String      domainName; // Can not use the name domain, reserved name in String class?

    /** The owner and admin for this app */
    @Basic
    private String      admin;

    /** An unique app user generated when the app is created. Most be provided in the request from the apps */
    @Basic
    private String      appUser;

    /** A generated app password. Must be provided in the requests from the apps */
    @Basic
    private String      appPassword;

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
        return String.format("{domain:%s, admin:%S appUser:%s, appPassword:%s, likeOncePerUser:%s, rateOncePerUser:%s}",
                getDomainName(), getAdmin(), getAppUser(), getAppPassword(), getOnlyLikeOncePerUser(), getOnlyRateOncePerUser());
    }


    // Getters and setters
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAppUser() {
        return appUser;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
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
