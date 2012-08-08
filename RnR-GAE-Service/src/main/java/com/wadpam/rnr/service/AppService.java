package com.wadpam.rnr.service;

import com.wadpam.rnr.dao.DAppSettingsDao;
import com.wadpam.rnr.datastore.Idempotent;
import com.wadpam.rnr.datastore.PersistenceManager;
import com.wadpam.rnr.domain.DAppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * This class implemented functionality related to creating and managing a RnR App.
 * @author mlv
 */
public class AppService {

    static final Logger LOG = LoggerFactory.getLogger(AppService.class);

    private PersistenceManager persistenceManager;

    private DAppSettingsDao appSettingsDao;

    static final int APPKEY_LENGTH = 30;
    static final String VALID_APPKEY_CHARS = "1234567890abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVXYZ";


    // Create new app settings for a specific domain/app
    @Idempotent
    @Transactional
    public DAppSettings createAppSettings(String domain, boolean onlyLikeOnce, boolean onlyRateOnce) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        LOG.debug("Create new app settings for domain " + domain);

        DAppSettings dAppSettings = appSettingsDao.findByPrimaryKey(domain);

        if (null == appSettingsDao.findByPrimaryKey(domain)) {
            // Create new app settings
            dAppSettings = new DAppSettings();
            // Only set these when created first time
            dAppSettings.setDomainName(domain);
            dAppSettings.setAppId(generateAppId(domain));
            dAppSettings.setAppKey(generateAppKey(domain));
        }

        // Update values
        dAppSettings.setOnlyLikeOncePerUser(onlyLikeOnce);
        dAppSettings.setOnlyRateOncePerUser(onlyRateOnce);

        // Store with cache
        persistenceManager.storeAppSettingsWithCache(dAppSettings);

        return dAppSettings;
    }

    // Generate app id, the MD5 hash of the domain string
    private String generateAppId(String domain) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        byte[] bytes = domain.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.digest(bytes);

        // Covert into a hex string
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
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

    // Get application settings
    public DAppSettings getAppSettings(String domain) {
        LOG.debug("Get app settings for domain " + domain);

        // Get from cache or db
        DAppSettings dAppSettings = persistenceManager.getAppSettingsWithCache(domain);

        return dAppSettings;
    }

    // Delete app settings
    public DAppSettings deleteAppSettings(String domain) {
        LOG.debug("Delete app settings for domain " + domain);

        DAppSettings dAppSettings = appSettingsDao.findByPrimaryKey(domain);
        if (null != dAppSettings) {
            persistenceManager.deleteAppSettingsWithCache(dAppSettings);
            return dAppSettings;
        }
        else
            return null;
    }

    // Generate new application key
    public DAppSettings generateNewAppKey(String domain) {
        LOG.debug("Generate new app key for domain " + domain);

        DAppSettings dAppSettings = appSettingsDao.findByPrimaryKey(domain);
        if (null != dAppSettings) {
            // Generate and store a new app key
            dAppSettings.setAppKey(generateAppKey(domain));
            persistenceManager.storeAppSettingsWithCache(dAppSettings);
            return dAppSettings;
        }
        else
            return null;
    }

    // Setters and Getters
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public void setAppSettingsDao(DAppSettingsDao appSettingsDao) {
        this.appSettingsDao = appSettingsDao;
    }
}
