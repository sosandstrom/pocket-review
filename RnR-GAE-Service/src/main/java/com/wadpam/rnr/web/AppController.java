package com.wadpam.rnr.web;


import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DApp;
import com.wadpam.rnr.json.JApp;
import com.wadpam.rnr.security.GaeUserDetails;
import com.wadpam.rnr.service.AppService;
import com.wadpam.server.exceptions.RestError;
import com.wadpam.server.web.AbstractRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The apps controller implements all REST methods related to managing applications.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="backoffice/app")
public class AppController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    private AppService appService;

    /**
     * Create a new app.
     * @param domain the domain for the new app
     * @param description an optional description of the app
     * @return redirect to the newly create app
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created app"),
            @RestCode(code=412, message="NOK", description="User reached the limited of the number of apps that can be created")
    })
    @RequestMapping(value="{domain}", method= RequestMethod.POST)
    public RedirectView createApp(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Principal principal,
                                          @PathVariable String domain,
                                          @RequestParam(required=false) String description) {


        DApp body = appService.createApp(domain, getCurrentUserEmail(), description);

        return new RedirectView(request.getRequestURI());
    }

    // Get the current user email from Spring security
    private String getCurrentUserEmail() {
        return getCurrentUserDetails().getEmail();
    }

    // Get the current user id from Spring security
    private String getCurrentUserId() {
        return getCurrentUserDetails().getUsername();
    }

    // Get Google app engine user from the security context
    private GaeUserDetails getCurrentUserDetails() {
        return (GaeUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Get app details.
     * @param domain the domain for the new app
     * @return the app for current domain
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=200, message="OK", description="App found"),
            @RestCode(code=404, message="NOK", description="No app found for that domain")
    })
    @RequestMapping(value="{domain}", method= RequestMethod.GET)
    public ResponseEntity<JApp> getApp(HttpServletRequest request,
                                       Principal principal,
                                       @PathVariable String domain) {

        DApp body = appService.getApp(domain);

        return new ResponseEntity<JApp>(Converter.convert(body), HttpStatus.OK);
    }

    /**
     * Update admins for the app.
     * @param domain the domain for the new app
     * @param emails a list of admin emails. At least one email must be provided
     * @return the app for current domain
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly updated app"),
            @RestCode(code=404, message="NOK", description="No app found for that domain")
    })
    @RequestMapping(value="{domain}", method= RequestMethod.POST, params ="email")
    public ResponseEntity<JApp> updateAdminsForApp(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   Principal principal,
                                                   @PathVariable String domain,
                                                   @RequestParam(required=true) String[] emails) {

        // At least one email should be provided
        if (emails.length < 1 ) {
            LOG.debug("At least one admin email must be provided");
            return new ResponseEntity<JApp>(HttpStatus.BAD_REQUEST);
        }

        // Make a simple format check of the emails
        for (int i = 0; i < emails.length; i++) {
            if (isValidEmail(emails[i]) == false) {
                LOG.debug("Admin email address invalid:{}", emails[i]);
                return new ResponseEntity<JApp>(HttpStatus.BAD_REQUEST);
            }
        }

        // Update the admin emails
        DApp dApp = appService.setAppAdmins(domain, Arrays.asList(emails));

        try {
            response.sendRedirect(request.getRequestURI());
            return null; // No need to return anything
        } catch (IOException e) {
            LOG.error("Not possible to redirect after creating application:{}", e.getMessage());
            return new ResponseEntity<JApp>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Simple validation of an email address
    private static boolean isValidEmail(String emailAddress) {
        return emailAddress.contains(" ") == false && emailAddress.matches(".+@.+\\.[a-z]+");
    }

    /**
     * Delete an app.
     * @param domain the domain for the app
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=200, message="OK", description="App deleted"),
            @RestCode(code=404, message="NOK", description="No app found for that domain")
    })
    @RequestMapping(value="{domain}", method= RequestMethod.DELETE)
    public ResponseEntity<JApp> deleteApp(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Principal principal,
                                          @PathVariable String domain) {

        DApp body = appService.deleteApp(domain);

        // TODO: What should we do with the data for that domain? Delete it?

        return new ResponseEntity<JApp>(HttpStatus.OK);
    }

    /**
     * Get all apps for the current user.
     * @return the list of apps
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=200, message="OK", description="App found")
    })
    @RequestMapping(value="my", method= RequestMethod.GET)
    public ResponseEntity<Collection<JApp>> getAllAppsForCurrentUser(HttpServletRequest request,
                                                                     Principal principal) {

        // Get user id from Spring context
        String adminEmail = getCurrentUserEmail();

        Collection<DApp> body = appService.getAllAppsForAppAdmin(adminEmail);

        return new ResponseEntity<Collection<JApp>>((Collection<JApp>)Converter.convert(body), HttpStatus.OK);
    }

    /**
     * Get all apps in the system.
     * @return a list of apps
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=200, message="OK", description="App found")
    })
    @RequestMapping(value="all", method= RequestMethod.GET)
    public ResponseEntity<Collection<JApp>> getAllApps(HttpServletRequest request,
                                                       Principal principal) {

        Collection<DApp> body = appService.getAllApps();

        return new ResponseEntity<Collection<JApp>>((Collection<JApp>)Converter.convert(body), HttpStatus.OK);
    }

    /**
     * Generate new api password.
     * @return redirect to the updated app
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=302, message="OK", description="Redirect to updated app")            ,
            @RestCode(code=404, message="NOK", description="No app found for that domain")
    })
    @RequestMapping(value="{domain}/password", method= RequestMethod.POST)
    public ResponseEntity<JApp> generateNewAppPassword(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       Principal principal,
                                                       @PathVariable String domain) {

        DApp body = appService.generateNewApiPassword(domain);

        // Figure out the base url
        String redirectUrl = null;
        Pattern pattern = Pattern.compile("^(.*)/password");
        Matcher matcher = pattern.matcher(request.getRequestURL().toString());
        if (matcher.find())
            redirectUrl = matcher.group(1);

        if (null == redirectUrl)
            throw new RestError(500, "Not possible to create base url for redirect after changing password");


        try {
            response.sendRedirect(redirectUrl);
            return null; // No need to do anything
        } catch (IOException e) {
            throw new RestError(500, "Not possible to redirect after changing password");
        }
    }


    // Setters and Getters
    public void setAppService(AppService appService) {
        this.appService = appService;
    }

}
