package com.wadpam.rnr.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker interface to annotating methods that are transactional and idempotent.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    // marker annotation
}
