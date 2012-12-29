package com.wadpam.rnr.web;

import com.wadpam.open.json.JCursorPage;
import com.wadpam.rnr.json.JRating;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Base class for GAE, Spring, RestTemplate integration tests.
 * Contains convenience methods and help classes.
 * @author mattiaslevin
 */
public abstract class AbstractRestTempleIntegrationTest {
    static final Logger LOG = LoggerFactory.getLogger(AbstractRestTempleIntegrationTest.class);


    // Spring rest template
    protected RestTemplate restTemplate;

    protected final String BASE_URL = getBaseUrl();

    // All sub classes must implement this method
    protected abstract String getBaseUrl();

    @Before
    public void setUp() {
        restTemplate = new RestTemplate();

        // Configure an error handler that does not throw exceptions
        // All http codes are handled and tested using asserts
        restTemplate.setErrorHandler(new DoNothingResponseErrorHandler());
    }


    // A error handler for Spring RestTemplate that ignores all http error codes.
    protected class DoNothingResponseErrorHandler extends DefaultResponseErrorHandler {
        @Override
        protected boolean hasError(HttpStatus statusCode) {
            return false;
        }
    }


    // Check that the response is a redirect (assert if not).
    // Extract the headers from from the response.
    protected class RedirectResponseExtractor implements ResponseExtractor<HttpHeaders> {
        @Override
        public HttpHeaders extractData(ClientHttpResponse clientHttpResponse) throws IOException {

            // Check that it is a redirect
            assertTrue("Redirect", clientHttpResponse.getStatusCode() == HttpStatus.FOUND);

            return clientHttpResponse.getHeaders();
        }
    }

    // Extract the status code from the response
    protected class StatusCodeResponseExtractor implements ResponseExtractor<Integer> {
        @Override
        public Integer extractData(ClientHttpResponse clientHttpResponse) throws IOException {

            return clientHttpResponse.getRawStatusCode();
        }
    }


    // This class writes a map to the request body in a RestTemplate execute method.
    protected class RequestCallBackBodyWriter implements RequestCallback {

        MultiValueMap<String, Object> map;

        protected RequestCallBackBodyWriter(MultiValueMap<String, Object> map) {
            this.map = map;
        }

        @Override
        public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
            StringBuilder builder = new StringBuilder();

            for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
                for (Object value : entry.getValue())
                    builder.append(entry.getKey()).append("=").append(value).append("&");
            }
            LOG.debug("Write POST body:{}", builder);

            PrintWriter out = new PrintWriter(clientHttpRequest.getBody());
            out.write(builder.toString()); // TODO add url encoding
            out.flush();
            out.close();
        }
    }

    // Make a post and follow the redirect
    protected  <T> ResponseEntity<T> postAndFollowRedirect(
            String url, MultiValueMap<String, Object> map, Class<T> clazz)
            throws MalformedURLException {

        HttpHeaders headers = restTemplate.execute(url, HttpMethod.POST,
                new RequestCallBackBodyWriter(map),
                new RedirectResponseExtractor());
        assertNotNull("Header", headers);
        assertNotNull(headers.getLocation().toURL());

        ResponseEntity<T> entity = restTemplate.getForEntity(headers.getLocation().toURL().toExternalForm(), clazz);
        assertEquals("Http response 200", HttpStatus.OK, entity.getStatusCode());

        return entity;
    }

    // Make a delete and follow the redirect
    protected  <T> ResponseEntity<T> deleteAndFollowRedirect(
            String url, MultiValueMap<String, Object> map, Class<T> clazz)
            throws MalformedURLException {

        HttpHeaders headers = restTemplate.execute(url, HttpMethod.DELETE,
                null,
                new RedirectResponseExtractor());
        assertNotNull("Header", headers);
        assertNotNull(headers.getLocation().toURL());

        ResponseEntity<T> entity = restTemplate.getForEntity(headers.getLocation().toURL().toExternalForm(), clazz);
        assertEquals("Http response 200", HttpStatus.OK, entity.getStatusCode());

        return entity;
    }

    // Get the http status code of the resource
    protected int getResourceStatusCode(String url, Object... urlVariables) {
        int statusCode = restTemplate.execute(url,
                HttpMethod.GET,
                null, // No need to modify the request before sending
                new StatusCodeResponseExtractor(),
                urlVariables);

        return statusCode;
    }


    // Check if a resource exist on the given url
    protected boolean doesResourceExist(String url, Object... urlVariables) {
        return getResourceStatusCode(url, urlVariables) == 200;
    }


    // Delete a resource at the given url
    protected boolean deleteResource(String url, Object ... urlVariables) {
        int statusCode = restTemplate.execute(url,
                HttpMethod.DELETE,
                null, // No need to modify the request before sending
                new StatusCodeResponseExtractor(),
                urlVariables);
        assertEquals("Http response 200", HttpStatus.OK.value(), statusCode);

        return true;
    }

    // Delete a resource at the given url and return the JSON response
    protected <T> ResponseEntity<T> deleteResource(String url, Class<T> clazz, Object ... urlVariables) {
        ResponseEntity<T> response = restTemplate.exchange(url,
                HttpMethod.DELETE,
                null,
                clazz,
                urlVariables);
        assertEquals("Http response 200", HttpStatus.OK, response.getStatusCode());

        return response;
    }

    // Delete a resource and verify that it was deleted
    protected boolean deleteResourceAndCheck(String url, Object... urlVariables) {
        deleteResource(url, urlVariables);
        assertFalse("Resource deleted", doesResourceExist(url, urlVariables));

        return true;
    }

    // Count the number of entities at the resource
    protected int countResources(String url, Object... urlVariables) {
        ResponseEntity<Collection> entity =
                restTemplate.getForEntity(url, Collection.class, urlVariables);
        assertEquals("Http response 200", HttpStatus.OK, entity.getStatusCode());

        return entity.getBody().size();
    }

    // Count the number of entities at the result page
    protected int countResourcesInPage(String url, Object... urlVariables) {
        ResponseEntity<JCursorPage> entity =
                restTemplate.getForEntity(url, JCursorPage.class, urlVariables);
        assertEquals("Http response 200", HttpStatus.OK, entity.getStatusCode());

        return entity.getBody().getItems().size();
    }





    // TODO Create builder

}
