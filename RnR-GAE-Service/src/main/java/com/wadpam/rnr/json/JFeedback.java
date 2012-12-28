package com.wadpam.rnr.json;

import com.wadpam.open.json.JBaseObject;
import com.wadpam.open.json.JLocation;

/**
 * Json representation of submitted user feedback.
 * @author mattiaslevin
 */
public class JFeedback extends JBaseObject {

    /** The feedback title */
    private String title;

    /** the feedback main description */
    private String feedback;

    /** A referenceId selected by the app */
    private String referenceId;

    /** Feedback category */
    private String category;

    /** Device model */
    private String deviceModel;

    /** Device OS */
    private String deviceOS;

    /** Device OS version */
    private String deviceOSVersion;

    /** User name of id of the person providing the feedback */
    private String username;

    /** user email of the person providing the feedback */
    private String userContact;

    /** The location of the user when the feedback was given */
    private JLocation location;


    // Setters and getters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
