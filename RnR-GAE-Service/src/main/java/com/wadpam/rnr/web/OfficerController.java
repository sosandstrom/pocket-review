package com.wadpam.rnr.web;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DOfficer;
import com.wadpam.rnr.json.JOfficer;
import com.wadpam.rnr.security.GaeUserDetails;
import com.wadpam.rnr.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The apps controller implements all REST methods related to managing backoffice users - officers.
 * @author mlv
 */
@Controller
@RequestMapping(value="backoffice/officer")
public class OfficerController {

    static final Logger LOG = LoggerFactory.getLogger(OfficerController.class);

    private AppService appService;

    /**
     * Login officer. Redirect to Google authentication if needed.
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=200, message="OK", description="Successful login"),
    })
    @RequestMapping(value="login", method= RequestMethod.GET)
    public ResponseEntity<JOfficer> loginOfficer(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 Principal principal) {

        try {
            // Figure out the base url
            String baseUrl = null;
            Pattern pattern = Pattern.compile("^(.*)/api/*");
            Matcher matcher = pattern.matcher(request.getRequestURL().toString());
            if (matcher.find())
                baseUrl = matcher.group(1);

            if (null == baseUrl)
                throw new IOException("Not possible to get base url");

            String destinationUrl = baseUrl + "/loggedin.html";

            // Check if the user is already logged in or not
            UserService userService = UserServiceFactory.getUserService();
            if (userService.isUserLoggedIn()) {
                LOG.debug("User already logged with Google, no need to login");
                response.sendRedirect(destinationUrl);
                return null; // Do nothing, the redirect handle things
            }

            // User not logged in, redirect to Google login page
            LOG.debug("Log in new user with Google");
            String googleLoginUrl = userService.createLoginURL(destinationUrl);
            response.sendRedirect(googleLoginUrl);
            return null; // Do nothing, the redirect handle things
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect url to Google with message: " + e.getMessage());
            return new ResponseEntity<JOfficer>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Logout officer. Redirect to Google if needed.
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=302, message="OK", description="Successful logout"),
    })
    @RequestMapping(value="logout", method= RequestMethod.GET)
    public ResponseEntity<JOfficer> logoutOfficer(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  Principal principal) {

        try {
            // Figure out the base url
            String baseUrl = null;
            Pattern pattern = Pattern.compile("^(.*)/api/*");
            Matcher matcher = pattern.matcher(request.getRequestURL().toString());
            if (matcher.find())
                baseUrl = matcher.group(1);

            if (null == baseUrl)
                throw new IOException("Not possible to get base url");

            String destinationUrl = baseUrl + "/loggedout.html";

            // Check if user already is logged out
            UserService userService = UserServiceFactory.getUserService();
            if (!userService.isUserLoggedIn()) {
                LOG.debug("User not logged in with Google, no need to logout");
                response.sendRedirect(destinationUrl);
                return null; // Do nothing, the redirect handle things
            }

            LOG.debug("Logout Google user with email " + userService.getCurrentUser().getEmail());
            String googleLogoutUrl = userService.createLogoutURL(destinationUrl);
            response.sendRedirect(googleLogoutUrl);
            return null; // Do nothing, the redirect handle things
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect url to Google with message: " + e.getMessage());
            return new ResponseEntity<JOfficer>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new officer for the currently logged in Google user.
     * @param name optional. The users nickname that will be used as display name
     * @return redirect to the newly create user details
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created officer details")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public ResponseEntity<JOfficer> createOfficer(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  Principal principal,
                                                  @RequestParam(required = false) String name) {

        try {
            // Get current user
            GaeUserDetails user = getCurrentUserDetails();
            if (null == user)
                return new ResponseEntity<JOfficer>(HttpStatus.UNAUTHORIZED);

            DOfficer body = appService.createOfficer(user.getUsername(), user.getEmail(), name, request.getRequestURL().toString());

            response.sendRedirect(request.getRequestURI());
            return null; // Do nothing, the redirect handle things
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect url for " + request.getRequestURI() + "after creating a new backoffice officer");
            return new ResponseEntity<JOfficer>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get the current user id from Spring security
    private String getCurrentUserId() {
        // Get the current user id from Spring security
        UserDetails userDetails = getCurrentUserDetails();
        if (null == userDetails)
            return null;
        else
            return userDetails.getUsername();
    }

    private GaeUserDetails getCurrentUserDetails() {
        // Get the current user id from Spring security
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof GaeUserDetails)
            return (GaeUserDetails)principal;
        else
            return null;
    }

    /**
     * Delete the officer for the currently logged in Google user.
     * @return the http response code indicating the outcome of the operation
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=200, message="OK", description="Officer deleted"),
            @RestCode(code=404, message="NOK", description="Officer details not found for user")
    })
    @RequestMapping(value="", method= RequestMethod.DELETE)
    public ResponseEntity<JOfficer> deleteCurrentOfficer(HttpServletRequest request,
                                                         HttpServletResponse response,
                                                         Principal principal) {

        // Get current user id
        String userId = getCurrentUserId();
        if (null == userId)
            return new ResponseEntity<JOfficer>(HttpStatus.UNAUTHORIZED);

        DOfficer body = appService.deleteOfficer(userId);
        if (null == body)
            return new ResponseEntity<JOfficer>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JOfficer>(HttpStatus.OK);
    }

    /**
     * Delete a specified officer.
     * @return the http response code indicating the outcome of the operation
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=200, message="OK", description="Officer deleted"),
            @RestCode(code=404, message="NOK", description="Officer details not found for user")
    })
    @RequestMapping(value="{userId}", method= RequestMethod.DELETE)
    public ResponseEntity<JOfficer> deleteOfficer(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  Principal principal,
                                                  @PathVariable String userId) {

        DOfficer body = appService.deleteOfficer(userId);
        if (null == body)
            return new ResponseEntity<JOfficer>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JOfficer>(HttpStatus.OK);
    }

    /**
     * Get officer details for the currently logged in Google user.
     * @return the officer details
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=200, message="OK", description="Officer details found"),
            @RestCode(code=404, message="NOK", description="Officer details not found for user")
    })
    @RequestMapping(value="", method= RequestMethod.GET)
    public ResponseEntity<JOfficer> getCurrentOfficer(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      Principal principal) {

        // Get user id from Spring context
        String userId = getCurrentUserId();
        if (null == userId)
            return new ResponseEntity<JOfficer>(HttpStatus.UNAUTHORIZED);

        DOfficer body = appService.getOfficer(userId);

        if (null == body)
            return new ResponseEntity<JOfficer>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JOfficer>(Converter.convert(body, request), HttpStatus.OK);
    }

    /**
     * Get officer details for a specific user.
     * @param userId the user id
     * @return the officer details
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=200, message="OK", description="Officer details found"),
            @RestCode(code=404, message="NOK", description="Officer details not found for user")
    })
    @RequestMapping(value="{userId}", method= RequestMethod.GET)
    public ResponseEntity<JOfficer> getOfficer(HttpServletRequest request,
                                               HttpServletResponse response,
                                               Principal principal,
                                               @PathVariable String userId) {

        DOfficer body = appService.getOfficer(userId);

        if (null == body)
            return new ResponseEntity<JOfficer>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JOfficer>(Converter.convert(body, request), HttpStatus.OK);
    }

    /**
     * Get all officers in the system.
     * @return a list of users
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=200, message="OK", description="Officer accounts found"),
    })
    @RequestMapping(value="all", method= RequestMethod.GET)
    // TODO: Should support pagination
    public ResponseEntity<Collection<JOfficer>> getAllOfficers(HttpServletRequest request,
                                                               HttpServletResponse response,
                                                               Principal principal) {

        Collection<DOfficer> body = appService.getAllOfficers();
        return new ResponseEntity<Collection<JOfficer>>((Collection<JOfficer>)Converter.convert(body, request), HttpStatus.OK);
    }

    /**
     * Change the status of an officer.
     * @param userId the user to change status for
     * @param status the new status
     *               1 - pending
     *               2 - active approved and active
     *               3 - suspended
     * @return redirect to the updated user details
     */
    @RestReturn(value=JOfficer.class, entity=JOfficer.class, code={
            @RestCode(code=302, message="OK", description="Redirect to the updated officer details")
    })
    @RequestMapping(value="{userId}/status/{status}", method= RequestMethod.POST)
    public ResponseEntity<JOfficer> setOfficerStatus(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     Principal principal,
                                                     @PathVariable String userId,
                                                     @PathVariable int status) {

        try {
            String accountStatus;
            switch (status) {
                case 0:
                    accountStatus = AppService.ACCOUNT_PENDING;
                    break;
                case 1:
                    accountStatus = AppService.ACCOUNT_ACTIVE;
                    break;
                case 2:
                    accountStatus = AppService.ACCOUNT_SUSPENDED;
                    break;
                default:
                    LOG.error("Trying to set account status to state not supported " + status);
                    return new ResponseEntity<JOfficer>(HttpStatus.BAD_REQUEST);
            }

            // Figure out the base url
            String redirectUrl = null;
            Pattern pattern = Pattern.compile("^(.*/officer/)*");
            Matcher matcher = pattern.matcher(request.getRequestURL().toString());
            if (matcher.find())
                redirectUrl = matcher.group(1);

            DOfficer body = appService.setOfficerStatus(userId, accountStatus, redirectUrl);

            if (null == body) {
                return new ResponseEntity<JOfficer>(HttpStatus.NOT_FOUND);
            }
            else {
                if (null == redirectUrl)
                    throw new IOException("Not possible to create base url");

                response.sendRedirect(redirectUrl);
                return null; // No need to do anything
            }
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect url after updating officer account status");
            return new ResponseEntity<JOfficer>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Setters and Getters
    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
