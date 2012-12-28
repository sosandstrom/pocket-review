package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.GeoPt;
import com.wadpam.open.exceptions.BadRequestException;
import com.wadpam.open.service.EmailSender;
import com.wadpam.open.transaction.Idempotent;
import com.wadpam.rnr.dao.DAppSettingsDao;
import com.wadpam.rnr.dao.DFeedbackDao;
import com.wadpam.rnr.dao.DFeedbackDaoBean;
import com.wadpam.rnr.domain.DAppSettings;
import com.wadpam.rnr.domain.DFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Service for handling user feedback and reporting of inappropriate content.
 * @author mattiaslevin
 */
public class FeedbackService {
    static final Logger LOG = LoggerFactory.getLogger(FeedbackService.class);

    // Offsets for exceptions
    public static final int ERR_BASE_FEEDBACK = RnrService.ERR_BASE_FEEDBACK;
    public static final int ERR_EMAIL_TO_FROM_MISSING = ERR_BASE_FEEDBACK + 1;


    // Optional default email that will be used to send feedback to regardless of domain
    private String toEmail;
    // Will be used as from email, must be set
    private String fromEmail;
    private String fromName;


    private DFeedbackDao feedbackDao;
    private DAppSettingsDao appSettingsDao;


    // Init
    public void init() {
        // Do nothing
    }


    // Feedback methods

    // Add new feedback
    @Idempotent
    @Transactional
    public DFeedback addFeedback(String domain, String title, String feedback, String referenceId,
                                 String category,
                                 String deviceModel, String deviceOS, String deviceOSVersion,
                                 String username, String userContact,
                                 Float latitude, Float longitude,
                                 String toEmail) {

        // Get application settings for this domain
        DAppSettings dAppSettings = appSettingsDao.findByPrimaryKey(domain);

        // Store feedback in datastore?
        boolean persist = true;
        if (null != dAppSettings && null != dAppSettings.getPersistFeedback()) {
            persist = dAppSettings.getPersistFeedback();
        }

        DFeedback dFeedback = null;
        if (persist) {

            // Is the user allowed to give feedback more then once in this domain?
            boolean onlyFeedbackOncePerUser = false;
            if (null != dAppSettings && null != dAppSettings.getOnlyFeedbackOncePerUser()) {
                onlyFeedbackOncePerUser = dAppSettings.getOnlyFeedbackOncePerUser();
            }
            if (null != username && onlyFeedbackOncePerUser) {
                // Check if the user already given feedback
                Iterable<DFeedback> dFeedbackIterable = feedbackDao.queryByUsername(username);
                if (null != dFeedbackIterable && dFeedbackIterable.iterator().hasNext()) {
                    dFeedback = dFeedbackIterable.iterator().next();
                }
            }

            if (null == dFeedback) {
                dFeedback = new DFeedback();
            }

            // Set values
            dFeedback.setTitle(title);
            dFeedback.setFeedback(feedback);
            dFeedback.setReferenceId(referenceId);
            dFeedback.setCategory(category);
            dFeedback.setDeviceModel(deviceModel);
            dFeedback.setDeviceOS(deviceOS);
            dFeedback.setDeviceOSVersion(deviceOSVersion);
            dFeedback.setUsername(username);
            dFeedback.setUserContact(userContact);
            if (null != latitude && null != longitude) {
                dFeedback.setLocation(new GeoPt(latitude, longitude));
            } else {
                dFeedback.setLocation(null);
            }

            feedbackDao.persist(dFeedback);
        }

        // Forward in email?
        boolean sendAsEmail = false;
        if (null != dAppSettings && null != dAppSettings.getSendFeedbackAsEmail()) {
            sendAsEmail = dAppSettings.getSendFeedbackAsEmail();
        }

        if (sendAsEmail) {
            // Get destination email address
            String destinationEmail = toEmail;
            if (null == destinationEmail) {
                destinationEmail = this.toEmail;
            }

            if (null != destinationEmail && null != fromEmail) {
                // Build the body
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(feedback).append("\n\n");
                stringBuilder.append("Domain:").append(domain).append("\n");
                stringBuilder.append("Category:").append(category).append("\n");
                stringBuilder.append("Reference Id:").append(referenceId).append("\n");
                stringBuilder.append("User name:").append(username).append("\n");
                stringBuilder.append("User contact:").append(userContact).append("\n");
                stringBuilder.append("Device model:").append(deviceModel).append("\n");
                stringBuilder.append("Device OS:").append(deviceOS).append("\n");
                stringBuilder.append("Device OS version:").append(deviceOSVersion).append("\n");
                stringBuilder.append("Latitude:").append(latitude).append("\n");
                stringBuilder.append("Longitude:").append(longitude).append("\n");

                EmailSender.sendEmail(destinationEmail, fromEmail, title, stringBuilder.toString());
            }
        }

        return dFeedback;
    }

    // Get user feedback
    public DFeedback getFeedback(String domain, Long id) {
        DFeedback dFeedback = feedbackDao.findByPrimaryKey(id);
        return dFeedback;
    }

    // Delete user feedback
    @Idempotent
    @Transactional
    public DFeedback deleteFeedback(String domain, Long id) {
        DFeedback dFeedback = feedbackDao.findByPrimaryKey(id);
        if (null != dFeedback) {
            feedbackDao.delete(dFeedback);
        }
        return dFeedback;
    }

    // Export user feedback newer or equal to the provided timetamp
    public void exportFeedback(String domain, String email, Long timestamp) throws IOException {

        // Check that we have from to and from email address
        if (null == this.fromEmail || null == email) {
            throw new BadRequestException(ERR_EMAIL_TO_FROM_MISSING,
                    String.format("Both to and from email address must be provided when exporting user feedback"));
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String[] columnNames = {
                DFeedbackDaoBean.COLUMN_NAME_TITLE,
                DFeedbackDaoBean.COLUMN_NAME_FEEDBACK,
                DFeedbackDaoBean.COLUMN_NAME_REFERENCEID,
                DFeedbackDaoBean.COLUMN_NAME_CATEGORY,
                DFeedbackDaoBean.COLUMN_NAME_USERNAME,
                DFeedbackDaoBean.COLUMN_NAME_USERCONTACT,
                DFeedbackDaoBean.COLUMN_NAME_DEVICEMODEL,
                DFeedbackDaoBean.COLUMN_NAME_DEVICEOS,
                DFeedbackDaoBean.COLUMN_NAME_DEVICEOSVERSION,
                DFeedbackDaoBean.COLUMN_NAME_LOCATION,
                DFeedbackDaoBean.COLUMN_NAME_CREATEDDATE
        };

        Iterable<DFeedback> dFeedback = feedbackDao.queryUpdatedAfter(timestamp);
        feedbackDao.writeAsCsv(baos, columnNames, dFeedback);
        baos.flush();
        LOG.info("Wrote CSV file size {} bytes", baos.size());

        // Send email
        EmailSender.sendEmail(this.fromEmail, "Backoffice admin",
                Arrays.asList(email), null, null,
                String.format("User feedback CSV export for %s", domain),
                null, "Please find all registered users in the attached CSV file.\nPlease delete export user feedback to reduce the data stored on the server",
                baos.toByteArray(), "user_feedback_export.csv", "text/csv");

        baos.close();
    }

    // Delete user feedback older the provided timestamp
    @Idempotent
    @Transactional
    public int deleteListOfFeedback(String domain, Long timestamp) {
        int numberDeleted = feedbackDao.deleteAllUpdatedBefore(timestamp);
        return numberDeleted;
    }


    // Report as inappropriate methods

    // Report as inappropriate
    public void reportAsInappropriate(String domain, String referenceId, String referenceDescription,
                                      String username, Float latitude, Float longitude) {

        // TODO

    }

    // Get inappropriate report
    public void getInappropriate(String domain, Long id) {

        // TODO
    }


    // Setters and Getters


    public void setAppSettingsDao(DAppSettingsDao appSettingsDao) {
        this.appSettingsDao = appSettingsDao;
    }

    public void setFeedbackDao(DFeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
}
