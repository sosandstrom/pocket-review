package com.wadpam.rnr.json;

/**
 * Json object for officers.
 * @author mlv
 */
public class JOfficer extends JBaseObject {

    /** The Google user unique user id */
    private String      userId;

    /** The Google users email*/
    private String      email;

    /** The users nickname */
    private String      name;

    /**
     * The status of the user account. Allowed values
     * -pending; Pending approval
     * -active; Account approved and active
     * -suspended; Account is suspended and can not be used
     */
    private String      accountStatus;

    /** The maximum number of apps this user can create */
    private Long        maxNumberOfApps;


    @Override
    public String subString() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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
