package com.wadpam.rnr.domain;

import net.sf.mardao.core.domain.AbstractCreatedUpdatedEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * This class contain settings that can be done per application
 * @author mattiaslevin
 */
@Entity
public class DAppSettings extends AbstractCreatedUpdatedEntity implements Serializable {

    private static final long serialVersionUID = 2237539236676704660L;


    /** The unique domain name for the app */
    @Id
    private String domainName;

    /** Decides if an identified user only can like a product once  */
    @Basic
    private Boolean onlyLikeOncePerUser = true;

    /** Decides if an identified user only can rate a product once  */
    @Basic
    private Boolean onlyRateOncePerUser = true;

    /** Decides if an identified user only can thumb up and down a product once  */
    @Basic
    private Boolean onlyThumbOncePerUser = true;

    /** Decides if a user is allowed to give feedback multiple times on the same reference if */
    @Basic
    private Boolean onlyFeedbackOncePerUser = false;

    /** Save user feedback in data store */
    @Basic
    private Boolean persistFeedback = true;

    /** Forward user feedback in email */
    @Basic
    private Boolean sendFeedbackAsEmail = false;


    @Override
    public String toString() {
        return String.format("{domain:%s, only like once:%b only rate once:%b only thumb one:%b}",
                domainName, onlyLikeOncePerUser, onlyRateOncePerUser, onlyThumbOncePerUser);
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

    public Boolean getOnlyThumbOncePerUser() {
        return onlyThumbOncePerUser;
    }

    public void setOnlyThumbOncePerUser(Boolean onlyThumbOncePerUser) {
        this.onlyThumbOncePerUser = onlyThumbOncePerUser;
    }

    public Boolean getOnlyFeedbackOncePerUser() {
        return onlyFeedbackOncePerUser;
    }

    public void setOnlyFeedbackOncePerUser(Boolean onlyFeedbackOncePerUser) {
        this.onlyFeedbackOncePerUser = onlyFeedbackOncePerUser;
    }

    public Boolean getPersistFeedback() {
        return persistFeedback;
    }

    public void setPersistFeedback(Boolean persistFeedback) {
        this.persistFeedback = persistFeedback;
    }

    public Boolean getSendFeedbackAsEmail() {
        return sendFeedbackAsEmail;
    }

    public void setSendFeedbackAsEmail(Boolean sendFeedbackAsEmail) {
        this.sendFeedbackAsEmail = sendFeedbackAsEmail;
    }
}
