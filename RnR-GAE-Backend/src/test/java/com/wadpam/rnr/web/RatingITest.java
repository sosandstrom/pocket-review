package com.wadpam.rnr.web;

import com.wadpam.open.AbstractRestTempleIntegrationTest;
import com.wadpam.rnr.json.JHistogram;
import com.wadpam.rnr.json.JProduct;
import com.wadpam.rnr.json.JRating;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for Ratings
 * @author mattiaslevin
 */
public class RatingITest extends AbstractRestTempleIntegrationTest {
    static final Logger LOG = LoggerFactory.getLogger(RatingITest.class);

    public RatingITest() {
    }

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
    // Test create and get rating
    public void testBasicRating() throws MalformedURLException {
        LOG.info("Test basic rating");

        final int rating = 40;
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A001");
        map.add("rating", rating);
        map.add("comment", "My comment");
        map.add("latitude", 11.11);
        map.add("longitude", 12.12);

        // Create and follow redirect
        ResponseEntity<JRating> entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Rating value is correct", rating, (long)entity.getBody().getRating());
    }

    @Test
    // Test create and redirect to product
    public void testBasicRatingRedirectToProduct() throws MalformedURLException {
        LOG.info("Test basic rating redirect to product");

        final int rating = 60;
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A002");
        map.add("rating", rating);

        // Create and follow redirect
        ResponseEntity<JProduct> entity = postAndFollowRedirect(BASE_URL + "product/rating", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Rating summary is correct", rating, (long)entity.getBody().getRatingSum());
    }


    @Test
    // Test delete rating
    public void testDeleteRating() throws MalformedURLException {
        LOG.info("Test delete rating");

        final String productId = "A003";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("rating", 60);

        // Create and follow redirect
        ResponseEntity<JRating> entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        String ratingId = entity.getBody().getId();
        boolean isDeleted = deleteResourceAndCheck(BASE_URL + "rating/{id}", ratingId);
        assertTrue("Rating was deleted", isDeleted);

        ResponseEntity<JProduct> productInfo =
                restTemplate.getForEntity(BASE_URL + "product/{id}", JProduct.class, productId);
        assertEquals("Response code 200", HttpStatus.OK, productInfo.getStatusCode());
        assertEquals("Rating count is 0", 0, (long)productInfo.getBody().getRatingCount());
    }

    // Test rate twice with same user name.
    // This should update the same rating, not create a new.
    @Test
    // Test create and get rating
    public void testRatingWithUsername() throws MalformedURLException {
        LOG.info("Test rating with user name");

        String username = "mlv";
        int rating = 50;
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A004");
        map.add("rating", rating);
        map.add("username", username);

        // Create and follow redirect
        ResponseEntity<JRating> entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Username is correct", username, entity.getBody().getUsername());

        // Rate one more time
        ResponseEntity<JProduct> productInfo = postAndFollowRedirect(BASE_URL + "product/rating", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Rating count is correct", 1, (long)productInfo.getBody().getRatingCount());
        // Make sure rating has not doubled
        assertEquals("Rating summary is correct", rating, (long)productInfo.getBody().getRatingSum());
    }

    @Test
    // Get all ratings for a user
    public void testGetRatingsForUsername() throws MalformedURLException {
        LOG.info("Test get all ratings for user name");

        String username = "mlv1";
        int rating = 50;
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B001");
        map.add("rating", rating);
        map.add("username", username);
        // Create and follow redirect
        ResponseEntity<JRating> entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B002");
        map.add("rating", rating);
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B003");
        map.add("rating", rating);
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Get all ratings
        assertEquals("Got 3 user ratings", 3, countResources(BASE_URL + "rating?username={username}", username));
    }


    @Test
    // Get all rating for a product
    public void testGetRatingsForProduct() throws MalformedURLException {
        LOG.info("Test get all ratings for product id");

        int rating = 50;
        String productId = "C001";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("rating", rating);
        // Create and follow redirect
        ResponseEntity<JRating> entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("rating", rating);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("rating", rating);
        // Create and follow redirect
        ResponseEntity<JProduct> productSummary = postAndFollowRedirect(BASE_URL + "product/rating", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, productSummary.getStatusCode());
        // Check to rating totals and average
        assertEquals("Rating count is correct", 3, (long)productSummary.getBody().getRatingCount());
        assertEquals("Rating summary is correct", rating * 3, (long)productSummary.getBody().getRatingSum());
        assertEquals("Rating average is correct", 50, (long)productSummary.getBody().getRatingAverage());

        // Get and count all ratings
        //assertEquals("Found 3 ratings", 3, countResourcesInPage(BASE_URL + "rating?productId={id}", productId));
    }


    @Test
    // Get histogram
    public void testGetRatingHistogram() throws MalformedURLException {
        LOG.info("Test get all ratings for product id");

        String productId = "D001";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("rating", 5);
        // Create and follow redirect
        ResponseEntity<JRating> entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("rating", 11);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("rating", 19);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("rating", 70);
        // Create and follow redirect
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "rating", map, JRating.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        ResponseEntity<JHistogram> histogram =
                restTemplate.getForEntity(BASE_URL + "rating/histogram?productId={id}", JHistogram.class, productId);
        assertEquals("Http response 200", HttpStatus.OK, histogram.getStatusCode());

        assertEquals("Got 3 different intervals", 3, histogram.getBody().getHistogram().size());
        long interval10To20 = histogram.getBody().getHistogram().get(10L);
        assertEquals("Interval 10 to 20 contains 2 ratings", 2, interval10To20);
    }

}
