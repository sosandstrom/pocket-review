package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.Email;
import com.wadpam.open.dao.DAppDomainDao;
import com.wadpam.open.service.NamespaceUtilities;
import com.wadpam.rnr.dao.DAppSettingsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class for creating default values in the database.
 * Putting this class in the backend module instead of the service module so different
 * backend integrations can have their own set of default values.
 * @author mattiaslevin
 */
public class DefaultDomains {
    static final Logger LOG = LoggerFactory.getLogger(DefaultDomains.class);

    private DAppDomainDao domainDao;
    private DAppSettingsDao settingsDao;

    public void init() {
        // Create default domains if they do no exist

        // Integration tests
        domainDao.persist("itest", null, null, null, "Domain for integration tests",
                new Email("mattias.levin@gmail.com"), "password", "iuser");
        // Create settings in the domain namespace
        NamespaceUtilities.runInNamespace("itest", new Runnable() {
            @Override
            public void run() {
                settingsDao.persist("itest", false, true, true, true, true, false);
            }
        });

        // Dev test
        domainDao.persist("dev", "UA-38370095-1", null, null, "Domain for dev tests",
                new Email("mattias.levin@gmail.com"), "password", "devuser");
        // Create settings in the domain namespace
        NamespaceUtilities.runInNamespace("dev", new Runnable() {
            @Override
            public void run() {
                settingsDao.persist("dev", false, true, true, true, true, true);
            }
        });
    }


    // Setters
    public void setDomainDao(DAppDomainDao domainDao) {
        this.domainDao = domainDao;
    }

    public void setSettingsDao(DAppSettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }
}
