package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.exceptions.NotFoundException;
import com.wadpam.open.json.JCursorPage;
import com.wadpam.open.web.AbstractRestController;
import com.wadpam.rnr.domain.DComment;
import com.wadpam.rnr.json.JComment;
import com.wadpam.rnr.service.RnrService;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * The feedback controller implements all REST methods related to feedback.
 * This feature can be used to provide registration form or feedback form.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/feedback")
public class FeedbackController extends AbstractRestController {
    static final Logger LOG = LoggerFactory.getLogger(FeedbackController.class);

    private boolean shouldSendEmail = true;
    private boolean shouldPersist = true;
    private RnrService rnrService;

    static final Converter CONVERTER = new Converter();

    /**
     * Send a feedback form to the backend.
     * The backend can either persist the feedback in the datastore,
     * forward it in an email or do both.
     * @param request
     * @param response
     * @param domain
     * @param title
     * @param feedback
     * @param category
     * @param username
     * @param email
     * @param latitude
     * @param longitude
     * @param deviceModel
     * @param deviceOS
     */
    @RestReturn(value=Void.class, entity=Void.class, code={
            @RestCode(code=200, message="OK", description="The feedback was logged")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public void ddComment(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable String domain,
                          @RequestParam String title,
                          @RequestParam String feedback,
                          @RequestParam(required=false) String category,
                          @RequestParam(required=false) String username,
                          @RequestParam(required=false) String email,
                          @RequestParam(required=false) Float latitude,
                          @RequestParam(required=false) Float longitude,
                          @RequestParam(required=false) String deviceModel,
                          @RequestParam(required=false) String deviceOS) {

        // TODO

    }


    /**
     * Export all persisted feedback to CSV file and forward it to the
     * provided email.
     * The actual export is done in a separate task and this method
     * will complete before.
     * @param request
     * @param response
     * @param email
     */
    @RestReturn(value=void.class, entity=void.class, code={
            @RestCode(code=200, message="OK", description="Export started")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="email")
    public void exportFeedback(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestParam String email) {

        // TODO
    }



    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

    public boolean isShouldPersist() {
        return shouldPersist;
    }

    public void setShouldPersist(boolean shouldPersist) {
        this.shouldPersist = shouldPersist;
    }

    public boolean isShouldSendEmail() {
        return shouldSendEmail;
    }

    public void setShouldSendEmail(boolean shouldSendEmail) {
        this.shouldSendEmail = shouldSendEmail;
    }
}
