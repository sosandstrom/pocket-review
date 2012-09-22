package com.wadpam.rnr.service;

import com.google.appengine.api.datastore.Email;
import com.wadpam.open.transaction.Idempotent;
import com.wadpam.rnr.dao.DAppAdminDao;
import com.wadpam.rnr.dao.DAppDao;
import com.wadpam.rnr.dao.DAppSettingsDao;
import com.wadpam.rnr.domain.DApp;
import com.wadpam.rnr.domain.DAppAdmin;
import com.wadpam.server.exceptions.BadRequestException;
import com.wadpam.server.exceptions.NotFoundException;
import com.wadpam.server.exceptions.RestError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * This class implemented functionality related to creating and managing a RnR App.
 * @author mattiaslevin
 */
public class AppService {

    static final Logger LOG = LoggerFactory.getLogger(AppService.class);

    static final int API_PASSWORD_LENGTH = 30;
    static final String API_PASSWORD_CHARS = "1234567890abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVXYZ";

    public static final String ACCOUNT_PENDING = "pending";
    public static final String ACCOUNT_ACTIVE = "active";
    public static final String ACCOUNT_SUSPENDED = "suspended";

    static final int DEFAULT_MAX_NUMBER_OF_APPS = 10;
    public static final String CREATE_ACCOUNT_START_STATE = ACCOUNT_ACTIVE;

    private DAppDao appDao;
    private DAppAdminDao appAdminDao;
    private EmailSender emailSender;

    private String createAccountStartState = CREATE_ACCOUNT_START_STATE;
    private int maxNumberOfAppsStartValue = DEFAULT_MAX_NUMBER_OF_APPS;


    /* App related methods */

    // Create new app for a specific domain
    @Idempotent
    @Transactional
    @PreAuthorize("hasPermission(#domain, 'isAppAdmin')")
    public DApp createApp(String domain, String adminEmail, String description) {
        LOG.debug("Create new app for domain:{}", domain);

        DApp dApp = appDao.findByPrimaryKey(domain);
        if (null == dApp) {

            // Check that the user has not reach the max number of apps
            Collection<DApp> dApps = appDao.findByAdminEmail(new Email(adminEmail));
            DAppAdmin dAppAdmin = appAdminDao.findByEmail(new Email(adminEmail));
            if (dApps.size() >= dAppAdmin.getMaxNumberOfApps())
                // This user is not allowed to create additional apps
                throw new BadRequestException(400, String.format("Admin have reached the limit of apps allowed:%s", dAppAdmin.getMaxNumberOfApps()));

            // Create new app
            dApp = new DApp();

            // Only set these when created first time
            Collection<Email> adminEmails = new ArrayList<Email>();
            adminEmails.add(new Email(adminEmail));
            dApp.setAppAdmins(adminEmails);

            dApp.setDomainName(domain);
            dApp.setApiUser(generateApiUser(domain));
            dApp.setApiPassword(generateApiPassword(domain));
        }

        // Properties that should be possible to update
        dApp.setDescription(description);

        // Store in datastore
        appDao.persist(dApp);

        return dApp;
    }

    // Generate api user, the MD5 hash of the domain string
    private String generateApiUser(String domain) {

        try {

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

        } catch (Exception e) {
            throw new RestError(500, String.format("Not possible to generate REST api user string with reason:%s", e.getMessage()));
        }
    }

    // Generate app password
    private String generateApiPassword(String domain) {
        Random rand = new Random();

        char[] key = new char[API_PASSWORD_LENGTH];
        for (int i = 0; i < API_PASSWORD_LENGTH; i++)
            key[i] = API_PASSWORD_CHARS.charAt(rand.nextInt(API_PASSWORD_CHARS.length()));

        return new String(key);
    }

    // Update list a of app admin emails
    @Idempotent
    @Transactional
    @PreAuthorize("hasPermission(#domain, 'isAppAdmin')")
    public DApp setAppAdmins(String domain, Collection<String> adminEmails) {
        LOG.debug("Set app admins:{} for domain:{}", adminEmails, domain);

        DApp dApp = getApp(domain);

        Collection<Email> emails = new ArrayList<Email>(adminEmails.size());
        for (String appAdmin : adminEmails)
            emails.add(new Email(appAdmin));

        dApp.setAppAdmins(emails);
        appDao.persist(dApp);

        return dApp;
    }

    // Get app details
    @PreAuthorize("hasPermission(#domain, 'isAppAdmin')")
    public DApp getApp(String domain) {
        LOG.debug("Get app for domain:{}", domain);

        DApp dApp = appDao.findByPrimaryKey(domain);

        if (null == dApp)
            throw new NotFoundException(404, String.format("No app found for domain:{}", domain));

        return dApp;
    }

    // Delete app
    @Idempotent
    @Transactional
    @PreAuthorize("hasPermission(#domain, 'isAppAdmin')")
    public DApp deleteApp(String domain) {
        LOG.debug("Delete app for domain:{}", domain);

        DApp dApp = appDao.findByPrimaryKey(domain);

        if (null == dApp)
            throw new NotFoundException(404, String.format("No app found for domain:{}", domain));

        appDao.delete(dApp);
        return dApp;
    }

    // Get all apps for a user
    public Collection<DApp> getAllAppsForAppAdmin(String adminEmail) {
        LOG.debug("Get all apps for admin with email:{}", adminEmail);
        Collection<DApp> apps = appDao.findByAdminEmail(new Email(adminEmail));
        return apps;
    }

    // Get all apps in the system
    public Collection<DApp> getAllApps() {
        LOG.debug("Get all apps in the system");

        // TODO: Support pagination
        Collection<DApp> apps = appDao.findAll();
        return apps;
    }

    // Generate new api password
    @Idempotent
    @Transactional
    @PreAuthorize("hasPermission(#domain, 'isAppAdmin')")
    public DApp generateNewApiPassword(String domain) {
        LOG.debug("Generate new api password for domain:{}", domain);

        DApp dApp = appDao.findByPrimaryKey(domain);

        if (null == dApp)
            throw new NotFoundException(404, String.format("No app found for domain:{}", domain));

        dApp.setApiPassword(generateApiPassword(domain));
        appDao.persist(dApp);

        return dApp;
    }


    /* App admin related methods */

    // Create a new app admin      <
    @Idempotent
    @Transactional
    @PreAuthorize("hasPermission(#adminEmail, 'isAdmin')")
    public DAppAdmin createAppAdmin(String adminEmail, String adminId, String name, String detailUrl) {
        LOG.debug("Create app admin with email:{} ", adminEmail);

        DAppAdmin dAppAdmin = appAdminDao.findByEmail(new Email(adminEmail));
        if (null == dAppAdmin) {
            // User does not exist
            dAppAdmin = new DAppAdmin();
            dAppAdmin.setAdminId(adminId);
            dAppAdmin.setEmail(new Email(adminEmail));
            dAppAdmin.setAccountStatus(createAccountStartState);
            dAppAdmin.setMaxNumberOfApps(new Long(maxNumberOfAppsStartValue));

            // Send email to indicate new officer joined
            StringBuilder sb = new StringBuilder();
            sb.append("A new admin just joined Pocket-Reviews.\n");
            sb.append("Name: " + name + "\n");
            sb.append("Email: " + dAppAdmin.getEmail() + "\n");
            sb.append(detailUrl);
            emailSender.sendEmailToAdmin("Pocket-Review have a new app admin", sb.toString());
        }

        // Update the name each time, not only when first created
        dAppAdmin.setName(name);

        // Store in datastore
        appAdminDao.persist(dAppAdmin);

        return dAppAdmin;
    }

    // Delete specified app admin
    @Idempotent
    @Transactional
    @PreAuthorize("hasPermission(#adminEmail, 'isAdmin')")
    public DAppAdmin deleteAppAdmin(String adminEmail) {
        LOG.debug("Remove admin with email:{}", adminEmail);

        DAppAdmin dAppAdmin = appAdminDao.findByEmail(new Email(adminEmail));

        if (null == dAppAdmin)
            throw new NotFoundException(404, String.format("No app admin found for email:{}", adminEmail));

        // Delete from datastore
        appAdminDao.delete(dAppAdmin);

        return dAppAdmin;
    }

    // Get details for a app admin
    @PreAuthorize("hasPermission(#adminEmail, 'isAdmin')")
    public DAppAdmin getAppAdmin(String adminEmail) {
        LOG.debug("Get details for admin with email:{}", adminEmail);

        DAppAdmin dAppAdmin = appAdminDao.findByEmail(new Email(adminEmail));

        if (null == dAppAdmin)
            throw new NotFoundException(404, String.format("No app admin found for email:{}", adminEmail));

        return dAppAdmin;
    }

    // Get all app admins in the system
    public Collection<DAppAdmin> getAllAppAdmins() {
        LOG.debug("Get all app admins in the system");

        Collection<DAppAdmin> dAppAdmins = appAdminDao.findAll();

        return dAppAdmins;
    }

    // Update app admin account status
    @Idempotent
    @Transactional
    @PreAuthorize("hasPermission(#adminEmail, 'isAdmin')")
    public DAppAdmin updateAdminAccountStatus(String adminEmail, String accountStatus) {
        LOG.debug("Set account status to:{} for app admin with email:{}", accountStatus, adminEmail);

        DAppAdmin dAppAdmin = appAdminDao.findByEmail(new Email(adminEmail));

        if (null == dAppAdmin)
            throw new NotFoundException(404, String.format("No app admin found for email:{}", adminEmail));

        // Update datastore
        dAppAdmin.setAccountStatus(accountStatus);
        appAdminDao.persist(dAppAdmin);

        return dAppAdmin;
    }

    // Setters and Getters
    public void setAppDao(DAppDao appDao) {
        this.appDao = appDao;
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void setAppAdminDao(DAppAdminDao appAdminDao) {
        this.appAdminDao = appAdminDao;
    }

    public void setCreateAccountStartState(String createAccountStartState) {
        this.createAccountStartState = createAccountStartState;
    }

    public void setMaxNumberOfAppsStartValue(int maxNumberOfAppsStartValue) {
        this.maxNumberOfAppsStartValue = maxNumberOfAppsStartValue;
    }
}
