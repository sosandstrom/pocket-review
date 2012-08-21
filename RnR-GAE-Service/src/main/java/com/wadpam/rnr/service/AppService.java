package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.Email;
import com.wadpam.rnr.dao.DAppDao;
import com.wadpam.rnr.dao.DOfficerDao;
import com.wadpam.rnr.datastore.Idempotent;
import com.wadpam.rnr.domain.DApp;
import com.wadpam.rnr.domain.DOfficer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Random;

/**
 * This class implemented functionality related to creating and managing a RnR App.
 * @author mlv
 */
public class AppService {

    static final Logger LOG = LoggerFactory.getLogger(AppService.class);

    private DOfficerDao officerDao;
    private DAppDao appDao;
    private EmailSender emailSender;

    static final int APPKEY_LENGTH = 30;
    static final String VALID_APPKEY_CHARS = "1234567890abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVXYZ";

    static final long DEFAULT_MAX_APPS = 5;

    public static final String ACCOUNT_PENDING = "pending";
    public static final String ACCOUNT_ACTIVE = "active";
    public static final String ACCOUNT_SUSPENDED = "suspended";


    // Create new app for a specific domain
    @Idempotent
    @Transactional
    public DApp createApp(String userId, String domain, boolean onlyLikeOnce, boolean onlyRateOnce) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        LOG.debug("Create new app settings for domain " + domain);

        // Special case, the domain backoffice is reserved as backoffice domain. No apps allowed
        if ("backoffice".equalsIgnoreCase(domain))
            throw new IllegalArgumentException("Not allowed to create application in domain with name backoffice");

        DApp dApp = appDao.findByDomainWithFixedNamespace(domain);
        if (null == dApp) {

            // Check that the user has not reach the max number of apps
            Collection<DApp> apps = appDao.findByAdmin(userId);
            DOfficer officer = officerDao.findByPrimaryKey(userId);
            if (apps.size() >= officer.getMaxNumberOfApps())
                // This user is not allowed to create additional apps
                throw new MaxNumberOfAppsReachedException("User have reached the limit of apps allowed: " + officer.getMaxNumberOfApps());

            // Create new app settings
            dApp = new DApp();
            // Only set these when created first time
            dApp.setAdmin(userId);
            dApp.setDomainName(domain);
            dApp.setAppUser(generateAppUser(domain));
            dApp.setAppPassword(generateAppPassword(domain));
        }

        // Update values
        dApp.setOnlyLikeOncePerUser(onlyLikeOnce);
        dApp.setOnlyRateOncePerUser(onlyRateOnce);

        // Store with cache
        appDao.persistWithFixedNamespace(dApp);

        return dApp;
    }

    // Generate app user, the MD5 hash of the domain string
    private String generateAppUser(String domain) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        byte[] bytes = domain.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.digest(bytes);

        // Covert into a hex string
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Generate app password
    private String generateAppPassword(String domain) {
        Random rand = new Random();

        char[] key = new char[APPKEY_LENGTH];
        for (int i = 0; i < APPKEY_LENGTH; i++)
            key[i] = VALID_APPKEY_CHARS.charAt(rand.nextInt(VALID_APPKEY_CHARS.length()));

        return new String(key);
    }

    // Get app details
    public DApp getApp(String domain) {
        LOG.debug("Get app settings for domain " + domain);
        DApp dApp = appDao.findByDomainWithFixedNamespace(domain);
        return dApp;
    }

    // Delete app
    public DApp deleteApp(String domain) {
        LOG.debug("Delete app settings for domain " + domain);

        DApp dApp = appDao.findByDomainWithFixedNamespace(domain);
        if (null != dApp) {
            appDao.deleteWithFixedNamespace(dApp);
            return dApp;
        }
        else
            return null;
    }

    // Get all apps for a user
    public Collection<DApp> getAllAppsForUser(String userId) {
        LOG.debug("Get all apps for current user");
        Collection<DApp> apps = appDao.findByAdmin(userId);
        return apps;
    }

    // Get all apps in the system
    public Collection<DApp> getAllApps() {
        LOG.debug("Get all apps in the system");

        // TODO: Support pagination
        Collection<DApp> apps = appDao.findAll();
        return apps;
    }

    // Generate new app password
    public DApp generateNewAppPassword(String domain) {
        LOG.debug("Generate new app password for domain " + domain);

        DApp dApp = appDao.findByDomainWithFixedNamespace(domain);
        if (null != dApp) {
            // Generate and store a new app key
            dApp.setAppPassword(generateAppPassword(domain));
            appDao.persistWithFixedNamespace(dApp);
            return dApp;
        }
        else
            return null;
    }

    // Create a new officer
    @Idempotent
    @Transactional
    public DOfficer createOfficer(String userId, String email, String name, String detailUrl) {
        LOG.debug("Create officer for Google user with email " + email);

        DOfficer dOfficer = officerDao.findByPrimaryKey(userId);
        if (null == dOfficer) {
            // User does not exist
            dOfficer = new DOfficer();
            dOfficer.setUserId(userId);
            dOfficer.setEmail(new Email(email));
            dOfficer.setAccountStatus(ACCOUNT_ACTIVE);
            dOfficer.setMaxNumberOfApps(DEFAULT_MAX_APPS);

            // Send email to indicate new officer joined
            StringBuilder sb = new StringBuilder();
            sb.append("A new user just joined Pocket-Reviews.\n");
            sb.append("Name: " + dOfficer.getName() + "\n");
            sb.append("Email: " + dOfficer.getEmail() + "\n");
            sb.append(detailUrl);
            emailSender.sendEmailToAdmin("Pocket-Review have a new officer", sb.toString());
        }

        // Update the name each time, not only when first created
        dOfficer.setName(name);

        // Store in datastore
        officerDao.persist(dOfficer);

        return dOfficer;
    }

    // Delete specified officer
    public DOfficer deleteOfficer(String userId) {
        LOG.debug("Remove user with id " + userId);

        DOfficer dOfficer = officerDao.findByPrimaryKey(userId);
        if (null == dOfficer)
            return null;

        // Delete from datastore
        officerDao.delete(dOfficer);

        return dOfficer;
    }

    // Get officer details for a specific user
    public DOfficer getOfficer(String userId) {
        LOG.debug("Get officer details for Google user with id " + userId);
        DOfficer dOfficer = officerDao.findByPrimaryKey(userId);
        return dOfficer;
    }

    // Get all officers in the system
    public Collection<DOfficer> getAllOfficers() {
        LOG.debug("Get all officers in the system");
        Collection<DOfficer> dOfficers = officerDao.findAll();
        return dOfficers;
    }


    // Update the officer account status
    public DOfficer setOfficerStatus(String userId, String accountStatus, String detailUrl) {
        LOG.debug("Update officer status to " + accountStatus + " for Google user with id " + userId);

        DOfficer dOfficer = officerDao.findByPrimaryKey(userId);
        if (null == dOfficer)
            return null;

        // Update datastore
        dOfficer.setAccountStatus(accountStatus);
        officerDao.persist(dOfficer);

        return dOfficer;
    }

    // Setters and Getters
    public void setAppDao(DAppDao appDao) {
        this.appDao = appDao;
    }

    public void setOfficerDao(DOfficerDao officerDao) {
        this.officerDao = officerDao;
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }
}
