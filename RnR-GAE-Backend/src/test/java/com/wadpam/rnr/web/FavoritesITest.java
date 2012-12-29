package com.wadpam.rnr.web;

import com.wadpam.rnr.json.JComment;
import com.wadpam.rnr.json.JFavorites;
import com.wadpam.rnr.json.JLike;
import com.wadpam.rnr.json.JProduct;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for Favorites.
 * @author mattiaslevin
 */
public class FavoritesITest extends AbstractRestTempleIntegrationTest {

    @Override
    protected String getBaseUrl() {
        return "http://localhost:8888/api/dev/";
    }

    @Test
    // Test create and get a favorite
    public void testBasicFavorites() throws MalformedURLException {
        LOG.info("Test basic favorites");

        String productId = "A001";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);

        // Create and follow redirect
        ResponseEntity<JFavorites> entity = postAndFollowRedirect(BASE_URL + "favorites/mlv", map, JFavorites.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertTrue("Found favorite", entity.getBody().getProductIds().contains(productId));
    }

    @Test
    // Test delete favorite
    public void testDeleteFavorite() throws MalformedURLException {
        LOG.info("Test delete favorite");

        final String productId = "A002";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        // Create and follow redirect
        ResponseEntity<JFavorites> entity = postAndFollowRedirect(BASE_URL + "favorites/mlv", map, JFavorites.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertTrue("Found favorite", entity.getBody().getProductIds().contains(productId));

        // Delete and follow redirect
        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        String deleteUrl = String.format("favorites/mlv?productId=%s", productId);
        entity = deleteAndFollowRedirect(BASE_URL + deleteUrl, map, JFavorites.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        assertFalse("Favorite deleted", entity.getBody().getProductIds().contains(productId));
    }

}
