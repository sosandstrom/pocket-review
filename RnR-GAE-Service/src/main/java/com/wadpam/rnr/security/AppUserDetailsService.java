package com.wadpam.rnr.security;

import com.google.appengine.api.NamespaceManager;
import com.wadpam.rnr.datastore.PersistenceManager;
import com.wadpam.rnr.domain.DAppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements a UserDetailsService used by Spring security when authenticating
 * applications accessing the REST interface.
 */
public class AppUserDetailsService implements UserDetailsService {

    static final Logger LOG = LoggerFactory.getLogger(AppUserDetailsService.class);

    private PersistenceManager persistenceManager;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.debug("Get UserDetails for user " + username);

        // Get the domain through the namespace
        final String domain = NamespaceManager.get();
        LOG.debug("Domain/namespace name " + domain);

        DAppSettings dAppSettings = persistenceManager.getAppSettingsWithCache(domain);
        if (null != dAppSettings) {

            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(1);
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_APP"));

            UserDetails userDetails = new User(dAppSettings.getAppId(), dAppSettings.getAppKey(), grantedAuthorities);

            return userDetails;
        } else {
            LOG.info("Trying to authenticate towards a domain that does not exist");
            return null;
        }
    }


    // Setters and getters
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
}
