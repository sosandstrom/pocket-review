package com.wadpam.rnr.service;

/**
 * Indicate the the user have create the maximum number of apps allowed according to his settings.
 */
public class MaxNumberOfAppsReachedException extends RuntimeException {

    public MaxNumberOfAppsReachedException(String message) {
        super(message);
    }

}
