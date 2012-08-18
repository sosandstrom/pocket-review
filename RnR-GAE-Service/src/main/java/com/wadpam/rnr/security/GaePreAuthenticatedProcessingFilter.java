package com.wadpam.rnr.security;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.wadpam.rnr.dao.DAppAdminDao;
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
 * @author mlv
 */
public class GaePreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    static final Logger LOG = LoggerFactory.getLogger(GaePreAuthenticatedProcessingFilter.class);


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        LOG.debug("Get pre-authenticated user principles");

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

        // We need to set credentials, otherwise the user is rejected
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(1);
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_TEMP"));

        return grantedAuthorities;
    }
}
