package com.wadpam.rnr.json;

import com.wadpam.open.json.JBaseObject;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 9/9/12
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class JAppSettings extends JBaseObject {


    /** The unique domain name for the app */
    private String      domain;

    /** Decides if an identified user only can like a product once  */
    private boolean     onlyLikeOncePerUser = true;

    /** Decides if an identified user only can rate a product once  */
    private boolean     onlyRateOncePerUser = true;

    @Override
    public String subString() {
        return String.format("{domain:%s, likeOncePerUser:%d, rateOncePerUser:%d}",
                getDomain(),isOnlyLikeOncePerUser(), isOnlyLikeOncePerUser());
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
}
