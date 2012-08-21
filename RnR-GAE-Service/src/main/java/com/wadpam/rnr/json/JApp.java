package com.wadpam.rnr.json;

/**
 * Json object for app settings.
 * @author mlv
 */
public class JApp extends JBaseObject {

    /** The unique domain name for the app */
    private String      domain;

    /** The owner and admin for this app */
    private String      admin;

    /** An unique app user generated when the app is created. Most be provided in the request from the apps */
    private String      appUser;

    /** A generated app password. Must be provided in the requests from the apps */
    private String      appPassword;

    /** Decides if an identified user only can like a product once  */
    private boolean     onlyLikeOncePerUser = true;

    /** Decides if an identified user only can rate a product once  */
    private boolean     onlyRateOncePerUser = true;


    @Override
    public String subString() {
        return String.format("{domain:%s, appUser:%s, appPassword:%s, likeOncePerUser:%d, rateOncePerUser:%d}",
                getDomain(), getAppUser(), getAppPassword(), isOnlyLikeOncePerUser(), isOnlyLikeOncePerUser());
    }

    // Setters and Getters
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
