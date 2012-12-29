package com.wadpam.rnr.web;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Integration tests for polling.
 * @author mattiaslevin
 */
public class PollITest extends AbstractRestTempleIntegrationTest {

    @Override
    protected String getBaseUrl() {
        return "http://localhost:8888/api/dev/";
    }

    @Test
    public void dummyTest() {
        assertTrue("Dummy test", true);
    }
}
