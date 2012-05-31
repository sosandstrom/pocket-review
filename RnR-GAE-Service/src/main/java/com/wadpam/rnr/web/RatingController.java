package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.json.JResult;
import com.wadpam.rnr.service.RnrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author os
 */
@Controller
@RequestMapping(value="{domain}/rating")
public class RatingController {
    static final Logger LOG = LoggerFactory.getLogger(RatingController.class);
    
    private RnrService rnrService;

    /**
     * Adds the rating for specified productId and username
     * @param productId domain-unique id for the product to rate
     * @param username optional
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @param rating 0..100
     * @return the new average for this product
     */
    @RestReturn(value=JResult.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Rating added")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.POST)
    public ResponseEntity<JResult> addRating(
            @PathVariable String productId,
            @RequestParam(required=false) String username,
            @RequestParam(required=false) Float latitude,
            @RequestParam(required=false) Float longitude,
            @RequestParam int rating) {
        final JResult body = rnrService.addRating(productId, username, 
                latitude, longitude, rating);
        return new ResponseEntity<JResult>(body, HttpStatus.OK);
    }

    /**
     * 
     * @param productId domain-unique id for the product to rate
     * @return the average rating for specified productId
     */
    @RestReturn(value=JResult.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Rating added"),
        @RestCode(code=404, message="Not Found", description="Product not found")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.GET)
    public ResponseEntity<JResult> getAverageRatings(
            @PathVariable String productId) {
        final JResult body = rnrService.getAverage(productId);
        if (null == body) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<JResult>(body, HttpStatus.OK);
    }

    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
