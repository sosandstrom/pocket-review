package com.wadpam.rnr.security;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.wadpam.rnr.domain.DAppAdmin;
import com.wadpam.rnr.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements a gae pre-authenticated filter that will check that are user is a logged in Google user.
 * @author mattiaslevin
 */
public class GaePreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    static final Logger LOG = LoggerFactory.getLogger(GaePreAuthenticatedProcessingFilter.class);


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        LOG.debug("Get pre-authenticated user principle");

        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn())
            // User has not logged in on Google
            return null;

        return userService.getCurrentUser();
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        LOG.debug("Get pre-authenticated user credentials");

        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn())
            // User has not logged in on Google
            return null;

        // Figure out the role
        if (userService.isUserAdmin()) {
            // If the user is a registered as admin for the GAE application set role to admin
            return "ROLE_ADMIN";
        } else {
            // Normal app admin
            return "ROLE_USER";
        }
    }
}
