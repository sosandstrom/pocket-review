package com.wadpam.rnr.web;

import com.wadpam.open.AbstractRestTempleIntegrationTest;
import com.wadpam.rnr.json.JComment;
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
 * Integration tests for comments.
 * @author mattiaslevin
 */
public class CommentITest extends AbstractRestTempleIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(CommentITest.class);

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
    // Test create and get comment
    public void testBasicComment() throws MalformedURLException {
        LOG.info("Test basic comment");

        final String comment = "My comment";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A001");
        map.add("comment", comment);
        map.add("latitude", 11.11);
        map.add("longitude", 12.12);

        // Create and follow redirect
        ResponseEntity<JComment> entity = postAndFollowRedirect(BASE_URL + "comment", map, JComment.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Comment is correct", comment, entity.getBody().getComment());
    }

    @Test
    // Test create and redirect to product
    public void testBasicCommentRedirectToProduct() throws MalformedURLException {
        LOG.info("Test basic comment redirect to product");

        final String comment = "My comment";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A002");
        map.add("comment", comment);

        // Create and follow redirect
        ResponseEntity<JProduct> entity = postAndFollowRedirect(BASE_URL + "product/comment", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertEquals("Comment count is correct", 1, (long)entity.getBody().getCommentCount());
    }

    @Test
    // Test delete comment
    public void testDeleteComment() throws MalformedURLException {
        LOG.info("Test delete comment");

        final String productId = "A003";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("comment", "My comment");

        // Create and follow redirect
        ResponseEntity<JComment> entity = postAndFollowRedirect(BASE_URL + "comment", map, JComment.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        String commentId = entity.getBody().getId();
        boolean isDeleted = deleteResourceAndCheck(BASE_URL + "comment/{id}", commentId);
        assertTrue("Comment was deleted", isDeleted);

        ResponseEntity<JProduct> productInfo =
                restTemplate.getForEntity(BASE_URL + "product/{id}", JProduct.class, productId);
        assertEquals("Response code 200", HttpStatus.OK, productInfo.getStatusCode());
        assertEquals("Comment count is 0", 0, (long)productInfo.getBody().getCommentCount());
    }

    @Test
    // Get all comments for a user
    public void testGetCommentsForUsername() throws MalformedURLException {
        LOG.info("Test get all comments for user name");

        String username = "mlv";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B001");
        map.add("comment", "My comment1");
        map.add("username", username);
        // Create and follow redirect
        ResponseEntity<JComment> entity = postAndFollowRedirect(BASE_URL + "comment", map, JComment.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B001");
        map.add("comment", "My comment2");
        map.add("username", username);
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "comment", map, JComment.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "B002");
        map.add("username", username);
        map.add("comment", "My comment");
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "comment", map, JComment.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Get all ratings
        assertEquals("Got 3 comments", 3, countResources(BASE_URL + "comment?username={username}", username));
    }


    @Test
    // Get all comments for a product
    public void testGetCommentsForProduct() throws MalformedURLException {
        LOG.info("Test get all comments for product id");

        String productId = "C001";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("comment", "My comment1");
        // Create and follow redirect
        ResponseEntity<JComment> entity = postAndFollowRedirect(BASE_URL + "comment", map, JComment.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("comment", "My comment2");
        // Create and follow redirect
        entity = postAndFollowRedirect(BASE_URL + "comment", map, JComment.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("comment", "My comment3");
        // Create and follow redirect
        ResponseEntity<JProduct> productSummary = postAndFollowRedirect(BASE_URL + "product/comment", map, JProduct.class);
        assertEquals("Response code 200", HttpStatus.OK, productSummary.getStatusCode());
        // Check to comments count
        assertEquals("Comments count is correct", 3, (long)productSummary.getBody().getCommentCount());

        // Get and count all comments
        assertEquals("Found 3 comments", 3, countResourcesInPage(BASE_URL + "comment?productId={id}", JComment.class, productId));
    }
}
