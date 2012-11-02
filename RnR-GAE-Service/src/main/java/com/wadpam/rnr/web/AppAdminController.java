package com.wadpam.rnr.web;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DAppAdmin;
import com.wadpam.rnr.json.JAppAdmin;
import com.wadpam.rnr.security.GaeUserDetails;
import com.wadpam.rnr.service.AppService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The apps controller implements all REST methods related to managing app admins.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="backoffice/admin")
public class AppAdminController {

    static final Logger LOG = LoggerFactory.getLogger(AppAdminController.class);
    static final Converter CONVERTER = new Converter();

    private AppService appService;

    private static final String LOGGEDIN_HTML = "/loggedin.html";
    private static final String LOGGEDOUT_HTML = "/loggedout.html";


    /**
     * Login app admin. Redirect to Google authentication if needed.
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=200, message="OK", description="Successful login")
    })
    @RequestMapping(value="login", method= RequestMethod.GET)
    public ResponseEntity<JAppAdmin> loginAdmin(HttpServletRequest request,
                                                HttpServletResponse response) {

        // Figure out the base url
        String baseUrl = null;
        Pattern pattern = Pattern.compile("^(.*)/api/*");
        Matcher matcher = pattern.matcher(request.getRequestURL().toString());
        if (matcher.find())
            baseUrl = matcher.group(1);

        if (null == baseUrl) {
            LOG.error("Not possible to create redirect URL when logging in admin");
            return new ResponseEntity<JAppAdmin>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String destinationUrl = baseUrl + LOGGEDIN_HTML;

        try {
            // Check if the user is already logged in or not
            UserService userService = UserServiceFactory.getUserService();
            if (userService.isUserLoggedIn()) {
                LOG.debug("User already logged with Google, no need to login");
                response.sendRedirect(destinationUrl);
                return null; // Do nothing, the redirect handle things
            }  else {
                // User not logged in, redirect to Google login page
                LOG.debug("Log in new user with Google");
                String googleLoginUrl = userService.createLoginURL(destinationUrl);
                response.sendRedirect(googleLoginUrl);
                return null; // Do nothing, the redirect handle things
            }
        }
        catch (IOException e) {
            LOG.error("Not possible to redirect user after login with reason:{}", e.getMessage());
            return new ResponseEntity<JAppAdmin>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Logout app admin. Redirect to Google if needed.
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=302, message="OK", description="Successful logout")
    })
    @RequestMapping(value="logout", method= RequestMethod.GET)
    public ResponseEntity<JAppAdmin> logoutAdmin(HttpServletRequest request,
                                                 HttpServletResponse response) {


        // Figure out the base url
        String baseUrl = null;
        Pattern pattern = Pattern.compile("^(.*)/api/*");
        Matcher matcher = pattern.matcher(request.getRequestURL().toString());
        if (matcher.find())
            baseUrl = matcher.group(1);

        if (null == baseUrl) {
            LOG.error("Not possible to create redirect URL when logging out admin");
            return new ResponseEntity<JAppAdmin>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String destinationUrl = baseUrl + LOGGEDOUT_HTML;

        try {
            // Check if user already is logged out
            UserService userService = UserServiceFactory.getUserService();
            if (!userService.isUserLoggedIn()) {
                LOG.debug("User not logged in with Google, no need to logout");
                response.sendRedirect(destinationUrl);
                return null; // Do nothing, the redirect handle things
            } else {
                LOG.debug("Logout Google user with email " + userService.getCurrentUser().getEmail());
                String googleLogoutUrl = userService.createLogoutURL(destinationUrl);
                response.sendRedirect(googleLogoutUrl);
                return null; // Do nothing, the redirect handle things
            }
        }
        catch (IOException e) {
            LOG.error("Not possible to redirect user after logout with reason:{}", e.getMessage());
            return new ResponseEntity<JAppAdmin>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new app admin for the currently logged in Google user.
     * @param name optional. The users nickname that will be used as display name
     * @return redirect to the newly create user details
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created admin details")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public ResponseEntity<JAppAdmin> createAdmin(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @RequestParam(required = false) String name) {

        // Get current user
        if (null == getCurrentUserDetails()) {
            LOG.debug("Trying to create an admin that is not logged in:{}", getCurrentUserEmail());
            return new ResponseEntity<JAppAdmin>(HttpStatus.UNAUTHORIZED);
        }

        appService.createAppAdmin(getCurrentUserEmail(), getCurrentUserId(), name, request.getRequestURL().toString());

        try {
            response.sendRedirect(request.getRequestURI());
            return null; // Do nothing, the redirect handle things
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect url after creating a new backoffice admin with reason:{}", e.getMessage());
            return new ResponseEntity<JAppAdmin>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
     * Delete the app admin for the currently logged in Google user.
     * @return the http response code indicating the outcome of the operation
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=200, message="OK", description="Admin deleted"),
            @RestCode(code=404, message="NOK", description="Admin not found for current user")
    })
    @RequestMapping(value="", method= RequestMethod.DELETE)
    public ResponseEntity<JAppAdmin> deleteCurrentAdmin(HttpServletRequest request,
                                                        HttpServletResponse response) {

        // Get current user
        if (null == getCurrentUserDetails()) {
            LOG.debug("Trying to delete an admin that is not logged in:{}", getCurrentUserEmail());
            return new ResponseEntity<JAppAdmin>(HttpStatus.UNAUTHORIZED);
        }

        appService.deleteAppAdmin(getCurrentUserEmail());

        return new ResponseEntity<JAppAdmin>(HttpStatus.OK);
    }

    /**
     * Delete a specified app admin.
     * @param email the admins email
     * @return the http response code indicating the outcome of the operation
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=200, message="OK", description="Admin deleted"),
            @RestCode(code=404, message="NOK", description="Admin not found for email")
    })
    @RequestMapping(value="{email}", method= RequestMethod.DELETE)
    public ResponseEntity<JAppAdmin> deleteAdmin(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @PathVariable String email) {

       appService.deleteAppAdmin(email);

       return new ResponseEntity<JAppAdmin>(HttpStatus.OK);
    }

    /**
     * Get app admin details for the currently logged in Google user.
     * @return the admin details
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=200, message="OK", description="Admin details found"),
            @RestCode(code=404, message="NOK", description="Admin details not found for current user")
    })
    @RequestMapping(value="", method= RequestMethod.GET)
    public ResponseEntity<JAppAdmin> getCurrentAdmin(HttpServletRequest request,
                                                     HttpServletResponse response) {

        // Get current user
        if (null == getCurrentUserDetails()) {
            LOG.debug("Trying to get for admin that is not logged in:{}", getCurrentUserEmail());
            return new ResponseEntity<JAppAdmin>(HttpStatus.UNAUTHORIZED);
        }

        DAppAdmin body = appService.getAppAdmin(getCurrentUserEmail());

        return new ResponseEntity<JAppAdmin>(CONVERTER.convert(body), HttpStatus.OK);
    }

    /**
     * Get app admin details for a specific user.
     * @param email the admins email
     * @return the officer details
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=200, message="OK", description="Admin details found"),
            @RestCode(code=404, message="NOK", description="Admin details not found for email")
    })
    @RequestMapping(value="{userId}", method= RequestMethod.GET)
    public ResponseEntity<JAppAdmin> getAdmin(HttpServletRequest request,
                                              HttpServletResponse response,
                                              @PathVariable String email) {

        DAppAdmin body = appService.getAppAdmin(email);

        return new ResponseEntity<JAppAdmin>(CONVERTER.convert(body), HttpStatus.OK);
    }

    /**
     * Get all app admins in the system.
     * @return a list of admins
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=200, message="OK", description="Admins found")
    })
    @RequestMapping(value="all", method= RequestMethod.GET)
    public ResponseEntity<Collection<JAppAdmin>> getAllAdmins(HttpServletRequest request,
                                                              HttpServletResponse response) {

        Iterable<DAppAdmin> dAppAdmins = appService.getAllAppAdmins();

        return new ResponseEntity<Collection<JAppAdmin>>((Collection<JAppAdmin>)CONVERTER.convert(dAppAdmins), HttpStatus.OK);
    }

    /**
     * Change the status of an app admin account.
     * @param email the admin email
     * @param status the new status
     *               0 - pending
     *               1 - active approved and active
     *               2 - suspended
     * @return redirect to the updated admin details
     */
    @RestReturn(value=JAppAdmin.class, entity=JAppAdmin.class, code={
            @RestCode(code=302, message="OK", description="Redirect to the updated admin details")
    })
    @RequestMapping(value="{userId}/status/{status}", method= RequestMethod.POST)
    public ResponseEntity<JAppAdmin> updateAdminAccountStatus(HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              @PathVariable String email,
                                                              @PathVariable int status) {

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
                LOG.error("Trying to set account status to state not supported:{}", status);
                return new ResponseEntity<JAppAdmin>(HttpStatus.BAD_REQUEST);
        }

        // Figure out the base url
        String redirectUrl = null;
        Pattern pattern = Pattern.compile("^(.*/admin/)*");
        Matcher matcher = pattern.matcher(request.getRequestURL().toString());
        if (matcher.find())
            redirectUrl = matcher.group(1);

        if (null == redirectUrl) {
            LOG.error("Not possible to create redirect url after updating admin account status");
            return new ResponseEntity<JAppAdmin>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        appService.updateAdminAccountStatus(email, accountStatus);

         try {
            response.sendRedirect(redirectUrl);
            return null; // No need to do anything
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect url after updating admin account status with reason:{}", e.getMessage());
            return new ResponseEntity<JAppAdmin>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Setters and Getters
    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
