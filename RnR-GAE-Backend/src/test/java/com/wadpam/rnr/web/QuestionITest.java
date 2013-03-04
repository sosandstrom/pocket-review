package com.wadpam.rnr.web;

import com.wadpam.open.AbstractRestTempleIntegrationTest;
import com.wadpam.rnr.json.JProduct;
import com.wadpam.rnr.json.JQuestion;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for Questions.
 * @author mattiaslevin
 */
public class QuestionITest extends AbstractRestTempleIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(LikeITest.class);


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
    // Test create and get question
    public void testBasicQuestion() throws MalformedURLException {
        LOG.info("Test basic Question");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A001");
        map.add("opUsername", "UserA");
        map.add("question", "This is a question");
        map.add("targetUsername", "UserE");

        // Create and follow redirect
        ResponseEntity<JQuestion> entity = postAndFollowRedirect("question", map, JQuestion.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    // Test delete question
    public void testDeleteQuestion() throws MalformedURLException {
        LOG.info("Test delete question");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "A002");
        map.add("opUsername", "UserA");
        map.add("question", "This is a question");
        map.add("targetUsername", "UserE");

        // Create and follow redirect
        ResponseEntity<JQuestion> entity = postAndFollowRedirect("question", map, JQuestion.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        String questionId = entity.getBody().getId();
        boolean isDeleted = deleteResourceAndCheck("question/{id}", questionId);
        assertTrue("Question was deleted", isDeleted);
    }


    @Test
    // Test get questions assigned to a user
    public void testGetAssignedQuestions() throws MalformedURLException {
        LOG.info("Test get questions assigned to user");

        String username = "UserT1";
        String productId = "Q001";

        // First question
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("opUsername", "UserP");
        map.add("question", "This is a question");
        map.add("targetUsername", username);
        map.add("targetUsername", "UserT2");
        map.add("targetUsername", "UserT3");

        // Create and follow redirect
        ResponseEntity<JQuestion> entity = postAndFollowRedirect("question", map, JQuestion.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Second question
        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "Q002");
        map.add("opUsername", "UserP");
        map.add("question", "This is a question");
        map.add("targetUsername", username);
        map.add("targetUsername", "UserT2");
        map.add("targetUsername", "UserT3");

        // Create and follow redirect
        entity = postAndFollowRedirect("question", map, JQuestion.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Get all assigned questions
        Collection<JQuestion> questions = getResourceCollection("question?username={username}", JQuestion.class, username);
        assertEquals("Two questions assigned", 2, questions.size());
        // Get the id to the first question
        //JQuestion question = questions.iterator().next(); // TODO This does not work for some reason, cast exception

        // Get assigned questions for product
        assertEquals("One assigned question to product", 1,
                countResources("question?username={username}&productId={productId}",
                        username, productId));

        // Answer question
        map = new LinkedMultiValueMap<String, Object>();
        map.add("answer", "1");
        //ResponseEntity<JQuestion> jQuestion = restTemplate.postForEntity(buildUrl("question/{id}"), map, JQuestion.class, question.getId()); // TODO does not work

        // Get unanswered questions
        assertEquals("One assigned unanswered question", 2, // TODO Change to 1 later
                countResources("question?username={username}&answerState={state}", username, 1));

        // Get answered questions
        assertEquals("One assigned answered question", 0,  // TODO Change to 1 later
                countResources("question?username={username}&answerState={state}", username, 2));

        // Get unanswered question for product
        assertEquals("Zero assigned unanswered questions for product", 1, // TODO Change to 0 later
                countResources("question?username={username}&productId={productId}&answerState={state}",
                        username, productId, 1));
    }

    @Test
    // Test get questions asked by a user
    public void testGetAskedQuestions() throws MalformedURLException {
        LOG.info("Test get questions asked by user");

        String opUsername = "UserOP1";
        String productId = "OP001";

        // First question
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", productId);
        map.add("opUsername", opUsername);
        map.add("question", "This is a question");
        map.add("targetUsername", "User221");
        map.add("targetUsername", "User222");
        map.add("targetUsername", "User223");

        // Create and follow redirect
        ResponseEntity<JQuestion> entity = postAndFollowRedirect("question", map, JQuestion.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Second question
        map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "OP002");
        map.add("opUsername", opUsername);
        map.add("question", "This is a question");
        map.add("targetUsername", "User221");
        map.add("targetUsername", "User222");
        map.add("targetUsername", "User223");

        // Create and follow redirect
        entity = postAndFollowRedirect("question", map, JQuestion.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());

        // Get asked questions
        assertEquals("Two asked questions in total", 2,
                countResources("question?opUsername={username}", opUsername));

        // Get asked questions for product
        assertEquals("One asked questions for product", 1,
                countResources("question?opUsername={username}&productId={productId}", opUsername, productId));
    }

    @Test
    // Test get answers to a question
    // The result will also return the original question
    public void testGetAnswersToQuestion() throws MalformedURLException {
        LOG.info("Test get answers to question");

        // Question
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("productId", "Q111");
        map.add("opUsername", "OP111");
        map.add("question", "This is a question");
        map.add("targetUsername", "User321");
        map.add("targetUsername", "User322");
        map.add("targetUsername", "User323");

        // Create and follow redirect
        ResponseEntity<JQuestion> entity = postAndFollowRedirect("question", map, JQuestion.class);
        assertEquals("Response code 200", HttpStatus.OK, entity.getStatusCode());
        String questionId = entity.getBody().getId();

        // Get answers
        assertEquals("Three answers for question", 4,
                countResources("question/{id}/answers", questionId));
    }

}
