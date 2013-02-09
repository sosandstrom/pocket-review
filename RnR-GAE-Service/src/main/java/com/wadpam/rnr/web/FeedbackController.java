package com.wadpam.rnr.web;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.analytics.google.GoogleAnalyticsTracker;
import com.wadpam.open.analytics.google.GoogleAnalyticsTrackerBuilder;
import com.wadpam.open.exceptions.NotFoundException;
import com.wadpam.open.exceptions.ServerErrorException;
import com.wadpam.open.web.AbstractRestController;
import com.wadpam.open.web.ValidationUtils;
import com.wadpam.rnr.domain.DFeedback;
import com.wadpam.rnr.json.JFeedback;
import com.wadpam.rnr.json.JInappropriate;
import com.wadpam.rnr.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * The feedback controller implements all REST methods related to feedback.
 * This feature can be used to provide registration form or feedback form.
 * @author mattiaslevin
 */
@Controller
public class FeedbackController extends AbstractRestController {
    static final Logger LOG = LoggerFactory.getLogger(FeedbackController.class);

    // Error codes
    public static final int ERR_FEEDBACK_NOT_FOUND = FeedbackService.ERR_BASE_FEEDBACK + 101;
    public static final int ERR_FEEDBACK_NOT_FOUND_DURING_DELETE = FeedbackService.ERR_BASE_FEEDBACK + 102;
    public static final int ERR_FEEDBACK_CREATE_FAILED = FeedbackService.ERR_BASE_FEEDBACK + 103;


    private FeedbackService feedbackService;

    static final Converter CONVERTER = new Converter();

    /**
     * Send a feedback form to the backend.
     *
     * The backend can either persist the feedback in the datastore or forward it in an email
     * or do both depending on configuration.
     * @param title the feedback title
     * @param feedback the feedback main description
     * @param referenceId a referenceId selected by the app. Could be reference to a
     *                    product id, application id or other content.
     *                    Must be unique within the app context.
     * @param category an optional category to allow the user the categorize the feedback e.g.
     *                 "quality", "service", price. Can also be used to allow better post processing
     *                 of the feedback.
     * @param deviceModel optional device model
     * @param deviceOS optional device OS
     * @param deviceOSVersion  optional device OS version
     * @param username optional user name of id of the person providing the feedback
     * @param userContact optional user contact data of the person providing the feedback.
     *                    This can be used
     *              to get back to the user.
     * @param latitude optional latitude of the user when the feedback was given
     * @param longitude optional longitude of the user when the feedback was given
     * @param toEmail optional email address the feedback should be sent to.
     *                Either application can provide this or an interceptor configured to
     *                inject the proper values based on application context.
     *                If this value is not set, the default to email will be used if configured,
     *                if not no email will be sent.
     * @return redirect to the newly created user feedback
     */
    @RestReturn(value=RedirectView.class, entity=RedirectView.class, code={
            @RestCode(code=302, message="OK", description="The feedback was logged")
    })
    @RequestMapping(value="{domain}/feedback", method= RequestMethod.POST)
    public RedirectView addFeedback(HttpServletRequest request,
                            HttpServletResponse response,
                            UriComponentsBuilder uriBuilder,
                            @ModelAttribute("email") String email,
                            @ModelAttribute("trackingCode") String trackingCode,
                            @PathVariable String domain,
                            @RequestParam String title,
                            @RequestParam String feedback,
                            @RequestParam String referenceId,
                            @RequestParam(required=false) String category,
                            @RequestParam(required=false) String deviceModel,
                            @RequestParam(required=false) String deviceOS,
                            @RequestParam(required=false) String deviceOSVersion,
                            @RequestParam(required=false) String username,
                            @RequestParam(required=false) String userContact,
                            @RequestParam(required=false) Float latitude,
                            @RequestParam(required=false) Float longitude) {

        // Create a tracker if tracking code is set
        GoogleAnalyticsTracker tracker = null;
        if (null != trackingCode) {
            LOG.debug("Create tracker with tracking code:{}", trackingCode);
            tracker = new GoogleAnalyticsTrackerBuilder()
                    .withNameAndTrackingCode(domain, trackingCode)
                    .withDeviceFromRequest(request)
                    .withVisitorId(username != null ? username.hashCode() : "anonymous".hashCode())
                    .build();
        }

        DFeedback dFeedback = feedbackService.addFeedback(domain, title, feedback, referenceId,
                category,
                deviceModel, deviceOS, deviceOSVersion,
                username, userContact,
                latitude, longitude,
                email, tracker);

        if (null == dFeedback) {
            throw new ServerErrorException(ERR_FEEDBACK_CREATE_FAILED,
                    "Failed to create user feedback");
        }

        return new RedirectView(uriBuilder.path("/{domain}/feedback/{id}")
                .buildAndExpand(domain, dFeedback.getId()).toUriString());
    }


    /**
     * Get one single user feedback details.
     * @param id the feedback to get
     */
    @RestReturn(value=JFeedback.class, entity=JFeedback.class, code={
            @RestCode(code=200, message="OK", description="Feedback found"),
            @RestCode(code=404, message="NOK", description="Feedback not found")
    })
    @RequestMapping(value="{domain}/feedback/{id}", method=RequestMethod.GET)
    public ResponseEntity<JFeedback> getFeedback(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @PathVariable String domain,
                                                 @PathVariable Long id) {

        DFeedback dFeedback = feedbackService.getFeedback(domain, id);

        if (null == dFeedback) {
            throw new NotFoundException(ERR_FEEDBACK_NOT_FOUND,
                    String.format("User feedback with id:%s not found", id));
        }

        return new ResponseEntity<JFeedback>(CONVERTER.convert(dFeedback), HttpStatus.OK);
    }


    /**
     * Export all persisted feedback to CSV file and forward it to the
     * provided email.
     *
     * The actual export is done in a separate task and this method
     * will complete immediately.
     * @param email the email address to send the exported feedback to
     * @param timestamp Optional. Export user feedback created/updated after
     *                  this timestamp.
     */
    @RestReturn(value=Void.class, entity=Void.class, code={
            @RestCode(code=200, message="OK", description="Export started")
    })
    @RequestMapping(value="{domain}/feedback/export", method=RequestMethod.GET, params="email")
    public void exportFeedback(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String domain,
                               @RequestParam String email,
                               @RequestParam(required=false) Long timestamp) {

        // Check the email format
        if (ValidationUtils.isValidEmailFormat(email)) {
            // Create a queue and add a task
            String workerUrl = String.format("/api/_worker/%s/feedback/export", domain);
            Queue queue = QueueFactory.getDefaultQueue();
            TaskOptions options = TaskOptions.Builder.withUrl(workerUrl).param("email", email);
            // Guard against timestamp being null
            if (null != timestamp) {
                options.param("timestamp", timestamp.toString());
            }
            queue.add(options);
            response.setStatus(HttpStatus.OK.value());
        } else {
            LOG.info("Tried to send exported user feedback to an invalid email:{}", email);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }


    /**
     * A GAE task for exporting user feedback to CSV format.
     * Clients should not use this REST method directly.
     * @param domain the domain
     * @param email The email the exported CSV file should be sent to
     * @param timestamp export user feedback create/updated after the
     *                  provided timestamp
     */
    @RestReturn(entity=Void.class, value=Void.class, code={
            @RestCode(code=200, description="The export process was started", message="OK")})
    @RequestMapping(value="_worker/{domain}/feedback/export", method= RequestMethod.POST,
            params="email", headers="X-AppEngine-TaskName")
    public void exportFeedbackWorker(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String domain,
                                     @RequestParam String email,
                                     @RequestParam(required=false) Long timestamp) throws Exception {

        try {
            feedbackService.exportFeedback(domain, email, timestamp);
        } catch (Throwable ex) {
            // Make sure we catch all
            // We do not want the worker thread to retry if something goes wrong
            // My end up in infinite loop
            LOG.info("Export user feedback to CSV failed with reason:{}", ex);
        }
    }


    /**
     * Delete one single user feedback.
     * @param id the feedback to delete
     */
    @RestReturn(value=Void.class, entity=Void.class, code={
            @RestCode(code=200, message="OK", description="Feedback deleted"),
            @RestCode(code=404, message="NOK", description="Feedback not found")
    })
    @RequestMapping(value="{domain}/feedback/{id}", method={RequestMethod.DELETE, RequestMethod.GET},
            params={"_method=DELETE"})
    public void deleteFeedback(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String domain,
                               @PathVariable Long id) {

        DFeedback dFeedback = feedbackService.deleteFeedback(domain, id);

        if (null == dFeedback) {
            throw new NotFoundException(ERR_FEEDBACK_NOT_FOUND_DURING_DELETE,
                    String.format("User feedback with id:%s not found", id));
        }
    }

    /**
     * Delete multiple user feedback.
     * @param timestamp Optional. Delete user feedback created/updated before
     *                  this timestamp
     */
    @RestReturn(value=Map.class, entity=Map.class, code={
            @RestCode(code=200, message="OK", description="Feedback deleted")
    })
    @RequestMapping(value="{domain}/feedback", method={RequestMethod.DELETE, RequestMethod.GET},
            params={"_method=DELETE"})
    @ResponseBody
    public Map deleteListOfFeedback(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String domain,
                                    @RequestParam(required=false) Long timestamp) {

        int rowsDeleted = feedbackService.deleteListOfFeedback(domain, timestamp);

        Map<String, Long> resp = new HashMap<String, Long>(1);
        resp.put("deleted", (long)rowsDeleted);
        return resp;
    }


    // Report as inappropriate methods

    /**
     * Report something as inappropriate.
     *
     * It is up to the app context to device what can be reported on. Could be content like comments
     * and images or something else that is being displayed in the app.
     * @param referenceId a referenceId selected by the app. Could be reference to a
     *                    product, application, comment, image etc. Must be unique within the app context.
     * @param referenceDescription An optional description of the reference id. This will allow for more easy
     *                             reading during the moderation.
     * @param username Optional user name
     * @param latitude optional latitude of the user when the report was done
     * @param longitude optional longitude of the user when the report was done
     */
    @RestReturn(value=Void.class, entity=Void.class, code={
            @RestCode(code=200, message="OK", description="Marked as inappropriate")
    })
    @RequestMapping(value="{domain}/inappropriate", method=RequestMethod.POST)
    public void inappropriate(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable String domain,
                              @RequestParam String referenceId,
                              @RequestParam(required=false) String referenceDescription,
                              @RequestParam(required=false) String username,
                              @RequestParam(required=false) Float latitude,
                              @RequestParam(required=false) Float longitude) {

        feedbackService.reportAsInappropriate(domain, referenceId, referenceDescription,
                username, latitude, longitude);

        // TODO
    }

    /**
     * Get one single inappropriate report.
     * @param id the report to get
     */
    @RestReturn(value=JInappropriate.class, entity=JInappropriate.class, code={
            @RestCode(code=200, message="OK", description="Feedback found"),
            @RestCode(code=404, message="NOK", description="Inappropriate report not found")
    })
    @RequestMapping(value="{domain}/inappropriate/{id}", method=RequestMethod.GET)
    public ResponseEntity<JInappropriate> getInapproriate(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     @PathVariable String domain,
                                                     @PathVariable Long id) {

        feedbackService.getInappropriate(domain, id);

        // TODO

        return new ResponseEntity<JInappropriate>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // TODO Delete

    // TODO set status - approved and rejected

    // TODO export / Send digest. Decide in business rules


    // Setters and Getters
    public void setFeedbackService(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }
}
