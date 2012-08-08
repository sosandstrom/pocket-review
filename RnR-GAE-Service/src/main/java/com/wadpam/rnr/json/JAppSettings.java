package com.wadpam.rnr.json;

/**
 * Json object for app settings.
 * @author mlv
 */
public class JAppSettings extends JBaseObject {

    /** The unique domain name for the app */
    private String      domain;

    /** An unique appId generated when the app is created. Most be provided in the request from the apps */
    private String      appId;

    /** A generated app key. Must be provided in the requests from the apps */
    private String      appKey;

    /** Decides if an identified user only can like a product once  */
    private boolean     onlyLikeOncePerUser = true;

    /** Decides if an identified user only can rate a product once  */
    private boolean     onlyRateOncePerUser = true;


    @Override
    public String toString() {
        return String.format("{domain:%s, appId:%s, appKey:%s, likeOncePerUser:%d, rateOncePerUser:%d}",
                getDomain(), getAppId(), getAppKey(), isOnlyLikeOncePerUser(), isOnlyLikeOncePerUser());
    }

    // Setters and Getters
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
}
