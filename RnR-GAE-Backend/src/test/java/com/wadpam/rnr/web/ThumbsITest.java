package com.wadpam.rnr.web;

import com.wadpam.open.AbstractRestTempleIntegrationTest;
import com.wadpam.rnr.json.JProduct;
import com.wadpam.rnr.json.JThumbs;
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
 * Integration tests fro Thumbs.
 * @author mattiaslevin
 */
public class ThumbsITest extends AbstractRestTempleIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(ThumbsITest.class);


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
    // Test create and get thumbs up and down
    public void testBasicThumbs() throws MalformedURLException {
        LOG.info("Test basic thumbs up and down");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A001");
        map.add("latitude", 11.11);
        map.add("longitude", 12.12);

        // Create and follow redirect
        ResponseEntity<JThumbs> entity = postAndFollowRedirect(BASE_URL + "thumbs/up", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "thumbs/down", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    // Test create and redirect to product
    public void testBasicThumbsRedirectToProduct() throws MalformedURLException {
        LOG.info("Test basic thumbs redirect to product");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A002");
        map.add("latitude", 11.11);
        map.add("longitude", 12.12);

        // Create and follow redirect
        ResponseEntity<JProduct> entity = postAndFollowRedirect(BASE_URL + "product/thumbs/up", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Thumbs up count is correct", 1, (long)entity.getBody().getThumbsUp());

        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "product/thumbs/down", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Thumbs down count is correct", 1, (long)entity.getBody().getThumbsDown());
    }

    @Test
    // Test delete thumbs
    public void testDeleteThumbs() throws MalformedURLException {
        LOG.info("Test delete thumbs");

        final String productId = "A003";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);

        // Create and follow redirect
        ResponseEntity<JThumbs> entity = postAndFollowRedirect(BASE_URL + "thumbs/up", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        String thumbsId = entity.getBody().getId();
        boolean isDeleted = deleteResourceAndCheck(BASE_URL + "thumbs/{id}", thumbsId);
        assertTrue("Thumbs up was deleted", isDeleted);

        ResponseEntity<JProduct> productInfo =
                restTemplate.getForEntity(BASE_URL + "product/{id}", JProduct.class, productId);
        assertEquals("Response code 200", HttpStatus.OK, productInfo.getStatusCode());
        assertEquals("Thumbs up count is 0", 0, (long)productInfo.getBody().getThumbsUp());

        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "thumbs/down", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        thumbsId = entity.getBody().getId();
        isDeleted = deleteResourceAndCheck(BASE_URL + "thumbs/{id}", thumbsId);
        assertTrue("Thumbs down was deleted", isDeleted);

        productInfo = restTemplate.getForEntity(BASE_URL + "product/{id}", JProduct.class, productId);
        assertEquals("Response code 200", HttpStatus.OK, productInfo.getStatusCode());
        assertEquals("Thumbs down count is 0", 0, (long)productInfo.getBody().getThumbsDown());
    }

    @Test
    // Get all thumbs for a user
    public void testGetThumbsForUsername() throws MalformedURLException {
        LOG.info("Test get all thumbs for user name");

        String username = "mlv";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B001");
        map.add("username", username);
        // Create and follow redirect
        ResponseEntity<JThumbs> entity = postAndFollowRedirect(BASE_URL + "thumbs/up", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B002");
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "thumbs/down", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B003");
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "thumbs/up", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Get all thumbs
        assertEquals("Got 3 thumbs", 3, countResources(BASE_URL + "thumbs?username={username}", username));
    }

    @Test
    // Check that a user can only thumb once per product
    public void testOnlyThumbOncePerUsername() throws MalformedURLException {
        LOG.info("Test that the same user only can like once per product");

        String username = "mlv";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "D001");
        map.add("username", username);
        // Create and follow redirect
        ResponseEntity<JProduct> entity = postAndFollowRedirect(BASE_URL + "product/thumbs/up", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Thumb up count is 1", 1, (long)entity.getBody().getThumbsUp());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "D001");
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "product/thumbs/up", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Thumb up count is 1", 1, (long)entity.getBody().getThumbsUp());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "D001");
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "product/thumbs/down", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Thumb up count is 0", 0, (long)entity.getBody().getThumbsUp());
        assertEquals("Thumb down count is 1", 1, (long)entity.getBody().getThumbsDown());
    }

    @Test
    // Get all thumbs for a product
    public void testGetThumbsForProduct() throws MalformedURLException {
        LOG.info("Test get all thumbs for product id");

        String productId = "C001";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        // Create and follow redirect
        ResponseEntity<JThumbs> entity = postAndFollowRedirect(BASE_URL + "thumbs/up", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "thumbs/down", map, JThumbs.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        // Create and follow redirect
        ResponseEntity<JProduct> productSummary = postAndFollowRedirect(BASE_URL + "product/thumbs/down", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, productSummary.getStatusCode());
        // Check the thumbs count
        assertEquals("Thumbs down count is correct", 2, (long)productSummary.getBody().getThumbsDown());

        // Get and count all likes
        assertEquals("Found 3 likes", 3, countResourcesInPage(BASE_URL + "like?productId={id}", JThumbs.class, productId));
    }

}
