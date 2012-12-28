package com.wadpam.rnr.json;

import com.wadpam.open.json.JBaseObject;

/**
 * Json representation of application settings.
 * @author mattiaslevin
 */
public class JAppSettings extends JBaseObject {


    /** The unique domain name for the app */
    private String domain;

    /** Decides if an identified user only can like a product once  */
    private boolean onlyLikeOncePerUser = true;

    /** Decides if an identified user only can rate a product once  */
    private boolean onlyRateOncePerUser = true;

    /** Decides if an identified user only can thumb up and down a product once  */
    private boolean onlyThumbOncePerUser = true;

    /** Decides if a user is allowed to give feedback multiple times on the same reference if */
    private boolean onlyFeedbackOncePerUser = false;

    /** Forward user feedback in email */
    private boolean sendFeedbackAsEmail = false;



    @Override
    public String subString() {
        return String.format("{domain:%s, likeOncePerUser:%b, rateOncePerUser:%b}",
                domain, onlyLikeOncePerUser, onlyRateOncePerUser);
    }


    // Setters and getters
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isOnlyLikeOncePerUser() {
        return onlyLikeOncePerUser;
    }

    public void setOnlyLikeOncePerUser(boolean onlyLikeOncePerUser) {
        this.onlyLikeOncePerUser = onlyLikeOncePerUser;
    }

    public boolean isOnlyRateOncePerUser() {
        return onlyRateOncePerUser;
    }

    public void setOnlyRateOncePerUser(boolean onlyRateOncePerUser) {
        this.onlyRateOncePerUser = onlyRateOncePerUser;
    }

    public Boolean getOnlyFeedbackOncePerUser() {
        return onlyFeedbackOncePerUser;
    }

    public void setOnlyFeedbackOncePerUser(Boolean onlyFeedbackOncePerUser) {
        this.onlyFeedbackOncePerUser = onlyFeedbackOncePerUser;
    }

    public Boolean getOnlyThumbOncePerUser() {
        return onlyThumbOncePerUser;
    }

    public void setOnlyThumbOncePerUser(Boolean onlyThumbOncePerUser) {
        this.onlyThumbOncePerUser = onlyThumbOncePerUser;
    }

    public boolean isSendFeedbackAsEmail() {
        return sendFeedbackAsEmail;
    }

    public void setSendFeedbackAsEmail(boolean sendFeedbackAsEmail) {
        this.sendFeedbackAsEmail = sendFeedbackAsEmail;
    }
}
