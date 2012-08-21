package com.wadpam.rnr.web;


import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.dao.DAppDao;
import com.wadpam.rnr.domain.DApp;
import com.wadpam.rnr.json.JApp;
import com.wadpam.rnr.security.GaeUserDetails;
import com.wadpam.rnr.service.AppService;
import com.wadpam.rnr.service.MaxNumberOfAppsReachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
 * The apps controller implements all REST methods related to managing RnR applications.
 * @author mlv
 */
@Controller
@RequestMapping(value="backoffice/app")
public class AppController {

    static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    private AppService appService;
    private DAppDao appDao;

    /**
     * Create a new app.
     * @param domain the domain for the new app
     * @param onlyLikeOnce optional. Should an identified user only be allowed to like once. Default true.
     * @param onlyRateOnce optional. Should an identified user only be allowed to rate once. Default true.
     * @return redirect to the newly create app
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created app"),
            @RestCode(code=412, message="NOK", description="User reached the limited of the number of apps that can be created")
})
    @RequestMapping(value="{domain}", method= RequestMethod.POST)
    public ResponseEntity<JApp> createApp(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Principal principal,
                                          @PathVariable String domain,
                                          @RequestParam(required=false, defaultValue="true") boolean onlyLikeOnce,
                                          @RequestParam(required=false, defaultValue="true") boolean onlyRateOnce) {

        try {
            // Check that the current user is allowed to operate on this domain
            if (!isCurrentUserAllowedToAccessDomain(domain))
                return new ResponseEntity<JApp>(HttpStatus.UNAUTHORIZED);

            // Get user id from Spring context
            String userId = getCurrentUserId();

            DApp body = appService.createApp(userId, domain, onlyLikeOnce, onlyRateOnce);

            response.sendRedirect(request.getRequestURI());
            return null; // No need to return anything
        }
        catch (MaxNumberOfAppsReachedException e) {
            // User reached the limit of the number of apps they can create
            return new ResponseEntity<JApp>(HttpStatus.PRECONDITION_FAILED);
        }
        catch (IllegalArgumentException e) {
            // User is create to create a domain with a reserved name
            return new ResponseEntity<JApp>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            // Everything else is server error
            LOG.error("Create app failed with message  " + e.getMessage());
            return new ResponseEntity<JApp>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if the current user is the owner of the app
    private boolean isCurrentUserOwnerOfApp(DApp app) {
        if (null == app)
            return true;

        // Compare the current user id with the owner id
        String userId = getCurrentUserId();
        if (null == userId || app.getAdmin().equalsIgnoreCase(userId))
            return true;
        else
            return false;
    }

    // Check if current user is owner of domain
    private boolean isCurrentUserAllowedToAccessDomain(String domain) {

        // Check if GAE admin then always true
        UserDetails userDetails = getCurrentUserUserDetails();
        if (userDetails != null && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            return true;

        // Get the app details for the domain
        DApp dApp = appDao.findByDomainWithFixedNamespace(domain);
        if (null == dApp)
            return true;

        // Check if the user is the owner of the app
        String userId = userDetails != null ? userDetails.getUsername() : null;
        if (null != userId || dApp.getAdmin().equalsIgnoreCase(userId))
            return true;
        else
            return false;
    }

    // Get the current user id from Spring security
    private String getCurrentUserId() {
        // Get the current user id from Spring security
        UserDetails userDetails = getCurrentUserUserDetails();
        if (null == userDetails)
            return null;
        else
            return userDetails.getUsername();
    }

    private UserDetails getCurrentUserUserDetails() {
        // Get the current user id from Spring security
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = null;
        if (principal instanceof GaeUserDetails)
            return (GaeUserDetails)principal;
        else
            return null;
    }

    /**
     * Get an app with its settings.
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

        // Check that the current user is allowed to operate on this domain
        if (!isCurrentUserOwnerOfApp(body))
            return new ResponseEntity<JApp>(HttpStatus.UNAUTHORIZED);

        if (null == body)
            return new ResponseEntity<JApp>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JApp>(Converter.convert(body, request), HttpStatus.OK);
    }

    /**
     * Delete an app.
     * @param domain the domain for the new app
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

        // Check that the current user is allowed to operate on this domain
        if (!isCurrentUserAllowedToAccessDomain(domain))
            return new ResponseEntity<JApp>(HttpStatus.UNAUTHORIZED);

        DApp body = appService.deleteApp(domain);

        // TODO: What should we do with the data for that domain? Delete it?

        if (null == body)
            return new ResponseEntity<JApp>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JApp>(HttpStatus.OK);
    }

    /**
     * Get all apps for the current user logged in to Google.
     * @return the list of apps
     */
    @RestReturn(value=JApp.class, entity=JApp.class, code={
            @RestCode(code=200, message="OK", description="App found")
    })
    @RequestMapping(value="my", method= RequestMethod.GET)
    public ResponseEntity<Collection<JApp>> getAllAppsForCurrentUser(HttpServletRequest request,
                                                                     Principal principal) {

        // Get user id from Spring context
        String userId = getCurrentUserId();
        if (null == getCurrentUserId())
            return new ResponseEntity<Collection<JApp>>(HttpStatus.UNAUTHORIZED);

        Collection<DApp> body = appService.getAllAppsForUser(userId);

        return new ResponseEntity<Collection<JApp>>((Collection<JApp>)Converter.convert(body, request), HttpStatus.OK);
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
        return new ResponseEntity<Collection<JApp>>((Collection<JApp>)Converter.convert(body, request), HttpStatus.OK);
    }

    /**
     * Generate new app password.
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

        try {
            // Check that the current user is allowed to operate on this domain
            if (!isCurrentUserAllowedToAccessDomain(domain))
                return new ResponseEntity<JApp>(HttpStatus.UNAUTHORIZED);

            DApp body = appService.generateNewAppPassword(domain);

            // Figure out the base url
            String redirectUrl = null;
            Pattern pattern = Pattern.compile("^(.*)/password");
            Matcher matcher = pattern.matcher(request.getRequestURL().toString());
            if (matcher.find())
                redirectUrl = matcher.group(1);

            if (null == body)
                return new ResponseEntity<JApp>(HttpStatus.NOT_FOUND);
            else {
                if (null == redirectUrl)
                    throw new IOException("Not possible to create base url");

                response.sendRedirect(redirectUrl);
                return null; // No need to do anything
            }
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect url after generating new app password for domain " + domain);
            return new ResponseEntity<JApp>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Setters and Getters
    public void setAppService(AppService appService) {
        this.appService = appService;
    }

    public void setAppDao(DAppDao appDao) {
        this.appDao = appDao;
    }
}
