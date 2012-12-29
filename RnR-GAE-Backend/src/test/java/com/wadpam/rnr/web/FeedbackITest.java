package com.wadpam.rnr.web;

import com.wadpam.rnr.json.*;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.MalformedURLException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for feedback.
 * @author mattiaslevin
 */
public class FeedbackITest extends AbstractRestTempleIntegrationTest {

    @Override
    protected String getBaseUrl() {
        return "http://localhost:8888/api/dev/";
    }

    @Test
    // Test create and get feedback
    public void testBasicFeedback() throws MalformedURLException {
        LOG.info("Test basic feedback");

        String title = "Title";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("title", title);
        map.add("feedback", "My feedback");
        map.add("referenceId", "12345");
        map.add("category", "Category");
        map.add("deviceModel", "Device model");
        map.add("deviceOS", "Device OS");
        map.add("deviceOSVersion", "Device OS version");
        map.add("username", "mlv");
        map.add("userContact", "mlv@xx.com");
        map.add("latitude", 11.11);
        map.add("longitude", 12.12);
        map.add("toEmail", "mattias.levin@gmail.com");

        // Create and follow redirect
        ResponseEntity<JFeedback> entity = postAndFollowRedirect(BASE_URL + "feedback", map, JFeedback.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Title is correct", title, entity.getBody().getTitle());
    }

    @Test
    // Test delete one single feedback
    public void testDeleteFeedback() throws MalformedURLException {
        LOG.info("Test delete feedback");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("title", "Title");
        map.add("feedback", "My feedback");
        map.add("referenceId", "12345");

        // Create and follow redirect
        ResponseEntity<JFeedback> entity = postAndFollowRedirect(BASE_URL + "feedback", map, JFeedback.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        String feedbackId = entity.getBody().getId();
        boolean isDeleted = deleteResourceAndCheck(BASE_URL + "feedback/{id}?_method=DELETE", feedbackId);
        assertTrue("Feedback was deleted", isDeleted);
    }

    @Test
    // Test export feedback
    public void testExportAllFeedback() throws MalformedURLException {
        LOG.info("Test export feedback");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("title", "Title1");
        map.add("feedback", "My feedback");
        map.add("referenceId", "12345");
        // Create and follow redirect
        ResponseEntity<JFeedback> entity = postAndFollowRedirect(BASE_URL + "feedback", map, JFeedback.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("title", "Title2");
        map.add("feedback", "My feedback");
        map.add("referenceId", "12345");
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "feedback", map, JFeedback.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("title", "Title3");
        map.add("feedback", "My feedback");
        map.add("referenceId", "12345");
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "feedback", map, JFeedback.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        ResponseEntity<String> response =
                restTemplate.getForEntity(BASE_URL + "feedback/export?email={email}",
                        String.class, "mattias.levin@gmail.com");
        assertEquals("Response code 200", HttpStatus.OK, response.getStatusCode());
    }

    @Test
    // Test delete all feedback
    public void testDeleteAllFeedback() throws MalformedURLException {
        LOG.info("Test delete all feedback");

        ResponseEntity<Map> entity = deleteResource(BASE_URL + "feedback?_method=DELETE", Map.class);
        LOG.info("Deleted rows:{}", entity.getBody());
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("4 rows deleted", 4, entity.getBody().get("deleted"));
    }

    // TODO Export with timestamp

    // TODO Delete with timestamp

}
