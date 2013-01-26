package com.wadpam.rnr.web;

import com.wadpam.open.AbstractRestTempleIntegrationTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

/**
 * Integration tests for polling.
 * @author mattiaslevin
 */
public class PollITest extends AbstractRestTempleIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(PollITest.class);


    @Override
    protected String getBaseUrl() {
        return "http://localhost:8888/api/itest/";
    }

    @Override
    protected String getUserName() {
        return "iuser";
    }

    @Override
    protected String getPassword() {
        return "password";
    }

    @Test
    public void dummyTest() {
        assertTrue("Dummy test", true);
    }
}
