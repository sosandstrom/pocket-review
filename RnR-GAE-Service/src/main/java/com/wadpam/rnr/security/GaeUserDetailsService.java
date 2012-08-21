package com.wadpam.rnr.security;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.wadpam.rnr.dao.DOfficerDao;
import com.wadpam.rnr.domain.DOfficer;
import com.wadpam.rnr.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements an AuthenticationUserDetailsService used by Spring security when authenticating
 * backoffice requests logged in using Google.
 * @author mlv
 */
public class GaeUserDetailsService implements AuthenticationUserDetailsService {

    static final Logger LOG = LoggerFactory.getLogger(GaeUserDetailsService.class);

    private DOfficerDao officerDao;


    @Override
    public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {
        User user = (User)authentication.getPrincipal();
        if (null == user) {
            // Should not happen
            LOG.error("Google user was not possible to obtain from the Authentication when loading user details");
            throw new UsernameNotFoundException("Not possible to get the Google user");
        }
        LOG.debug("Creating user details for Google user with email " + user.getEmail() );

        // Figure out the role
        Collection<GrantedAuthority> grantedAuthorities =  new ArrayList<GrantedAuthority>();

        UserService userService = UserServiceFactory.getUserService();
        if (userService.isUserAdmin()) {
            // If the user is a registered as admin for the GAE application set role to admin
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {

            // Check the account status in the datastore to decide role
            DOfficer dOfficer = officerDao.findByPrimaryKey(user.getUserId());

            if (null != dOfficer && dOfficer.getAccountStatus().equalsIgnoreCase(AppService.ACCOUNT_ACTIVE)) {
                // Normal backoffice user
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            } else {
                // Give all other types of user pending role
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PENDING"));
            }

        }
        LOG.debug("User credentials to " + grantedAuthorities);

        UserDetails userDetails = new GaeUserDetails(user, grantedAuthorities);
        return userDetails;
    }


    // Setters and getters
    public void setOfficerDao(DOfficerDao officerDao) {
        this.officerDao = officerDao;
    }
}
