package com.wadpam.rnr.web;

import com.wadpam.open.json.JMonitor;
import com.wadpam.rnr.json.JRating;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for Ratings
 * @author mattiaslevin
 */
public class RatingTest {

    static final Logger LOG = LoggerFactory.getLogger(RatingTest.class);

    static final String                  BASE_URL       = "http://localhost:888/api/dev/";

    RestTemplate                         template;

    public RatingTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        template = new RestTemplate();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRating() {
        LOG.debug("Test rating");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("productId", "A001");
        map.add("rating", "59");
        ResponseEntity<JRating> entity = template.postForEntity(BASE_URL + "rating", map, JRating.class);
        LOG.debug("Response code:{}", entity.getStatusCode());

        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
    }


}
