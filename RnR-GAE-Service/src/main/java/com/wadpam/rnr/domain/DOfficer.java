package com.wadpam.rnr.domain;

import com.google.appengine.api.datastore.Email;
import net.sf.mardao.api.domain.AEDStringEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Stores all application officers/admins.
 * @author mlv
 */
@Entity
public class DOfficer extends AEDStringEntity implements Serializable {


    /** The Google user unique user id */
    @Id
    private String      userId;

    /** The Google users email */
    @Basic
    private Email       email;

    /** The users nickname that will be used as display name */
    @Basic
    private String      name;

    /**
     * The status of the user account. Allowed values
     * -pending; Pending approval
     * -active; Account approved and active
     * -suspended; Account is suspended and can not be used
     */
    @Basic
    private String      accountStatus;

    /** The maximum number of apps this user can create */
    @Basic
    private Long        maxNumberOfApps = 5L;


    @Override
    public String getSimpleKey() {
        return getUserId();
    }

    @Override
    public String toString() {
        return String.format("{userId:%s, account status:%s, max number of apps:%d}",
                getUserId(), getAccountStatus(), getMaxNumberOfApps());
    }


    // Setters and getters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Long getMaxNumberOfApps() {
        return maxNumberOfApps;
    }

    public void setMaxNumberOfApps(Long maxNumberOfApps) {
        this.maxNumberOfApps = maxNumberOfApps;
    }
}
