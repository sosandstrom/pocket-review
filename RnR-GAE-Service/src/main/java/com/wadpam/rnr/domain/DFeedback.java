package com.wadpam.rnr.domain;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Domain object for user feedback.
 * @author mattiaslevin
 */
@Entity
public class DFeedback extends AbstractLongEntity {

    /** The feedback title */
    @Basic
    private String title;

    /** the feedback main description */
    @Basic
    private String feedback;

    /** A referenceId selected by the app */
    @Basic
    private String referenceId;

    /** Feedback category */
    @Basic
    private String category;

    /** Device model */
    @Basic
    private String deviceModel;

    /** Device OS */
    @Basic
    private String deviceOS;

    /** Device OS version */
    @Basic
    private String deviceOSVersion;

    /** User name of id of the person providing the feedback */
    @Basic
    private String username;

    /** user email of the person providing the feedback */
    @Basic
    private String userContact;

    /** The location of the user when the feedback was given */
    @Basic
    private GeoPt location;


    @Override
    public String toString() {
        return String.format("Ref id:%s, title:%s user:%s", referenceId, title, username);
    }


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

    public GeoPt getLocation() {
        return location;
    }

    public void setLocation(GeoPt location) {
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
