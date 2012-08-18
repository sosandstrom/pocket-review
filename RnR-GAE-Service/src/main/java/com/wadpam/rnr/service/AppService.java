package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.wadpam.rnr.dao.DAppAdminDao;
import com.wadpam.rnr.dao.DAppDao;
import com.wadpam.rnr.datastore.Idempotent;
import com.wadpam.rnr.domain.DApp;
import com.wadpam.rnr.domain.DAppAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
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

    private DAppAdminDao appAdminDao;
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
            DAppAdmin appAdmin = appAdminDao.findByPrimaryKey(userId);
            if (apps.size() >= appAdmin.getMaxNumberOfApps())
                // This user is not allowed to create additional apps
                throw new MaxNumberOfAppsReachedException("User have reached the limit of apps allowed: " + appAdmin.getMaxNumberOfApps());

            // Create new app settings
            dApp = new DApp();
            // Only set these when created first time
            dApp.setAdmin(userId);
            dApp.setDomainName(domain);
            dApp.setAppId(generateAppId(domain));
            dApp.setAppKey(generateAppKey(domain));
        }

        // Update values
        dApp.setOnlyLikeOncePerUser(onlyLikeOnce);
        dApp.setOnlyRateOncePerUser(onlyRateOnce);

        // Store with cache
        appDao.persistWithFixedNamespace(dApp);

        return dApp;
    }

    // Generate app id, the MD5 hash of the domain string
    private String generateAppId(String domain) throws UnsupportedEncodingException, NoSuchAlgorithmException {

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

    // Generate app key
    private String generateAppKey(String domain) {
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

    // Generate new app key
    public DApp generateNewAppKey(String domain) {
        LOG.debug("Generate new app key for domain " + domain);

        DApp dApp = appDao.findByDomainWithFixedNamespace(domain);
        if (null != dApp) {
            // Generate and store a new app key
            dApp.setAppKey(generateAppKey(domain));
            appDao.persistWithFixedNamespace(dApp);
            return dApp;
        }
        else
            return null;
    }

    // Create a new user
    @Idempotent
    @Transactional
    public DAppAdmin createUser(String userId, String email, String name, String detailUrl) {
        LOG.debug("Create app user for Google user with email " + email);

        DAppAdmin dAppAdmin = appAdminDao.findByPrimaryKey(userId);
        if (null == dAppAdmin) {
            // User does not exist
            dAppAdmin = new DAppAdmin();
            dAppAdmin.setUserId(userId);
            dAppAdmin.setEmail(new Email(email));
            dAppAdmin.setAccountStatus(ACCOUNT_ACTIVE);
            dAppAdmin.setMaxNumberOfApps(DEFAULT_MAX_APPS);

            // Send email to indicate pending new app admin needs approval
            StringBuilder sb = new StringBuilder();
            sb.append("A new user just joined Pocket-Reviews.\n");
            sb.append("Name: " + dAppAdmin.getName() + "\n");
            sb.append("Email: " + dAppAdmin.getEmail() + "\n");
            sb.append(detailUrl);
            emailSender.sendEmailToAdmin("Pocket-Review have a new user", sb.toString());
        }

        // Update the name each time, not only when first created
        dAppAdmin.setName(name);

        // Store in datastore
        appAdminDao.persist(dAppAdmin);

        return dAppAdmin;
    }

    // Delete specified user
    public DAppAdmin deleteUser(String userId) {
        LOG.debug("Remove user with id " + userId);

        DAppAdmin dAppAdmin = appAdminDao.findByPrimaryKey(userId);
        if (null == dAppAdmin)
            return null;

        // Delete from datastore
        appAdminDao.delete(dAppAdmin);

        return dAppAdmin;
    }

    // Get user details for a specific user
    public DAppAdmin getUser(String userId) {
        LOG.debug("Get user details for Google user with id " + userId);
        DAppAdmin dAppAdmin = appAdminDao.findByPrimaryKey(userId);
        return dAppAdmin;
    }

    // Get all users in the system
    public Collection<DAppAdmin> getAllUsers() {
        LOG.debug("Get all users in the system");
        Collection<DAppAdmin> dAppAdmins = appAdminDao.findAll();
        return dAppAdmins;
    }


    // Update the app admin account status
    public DAppAdmin setUserStatus(String userId, String accountStatus, String detailUrl) {
        LOG.debug("Update user status to " + accountStatus + " for Google user with id " + userId);

        DAppAdmin dAppAdmin = appAdminDao.findByPrimaryKey(userId);
        if (null == dAppAdmin)
            return null;

        // Update datastore
        dAppAdmin.setAccountStatus(accountStatus);
        appAdminDao.persist(dAppAdmin);

        return dAppAdmin;
    }

    // Setters and Getters
    public void setAppDao(DAppDao appDao) {
        this.appDao = appDao;
    }

    public void setAppAdminDao(DAppAdminDao appAdminDao) {
        this.appAdminDao = appAdminDao;
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }
}
