package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.json.JLike;
import com.wadpam.rnr.json.JProductV15;
import com.wadpam.rnr.json.JRating;
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

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;


/**
 * The rating controller implements all REST methods related to ratings and reviews
 * @author os
 */
@Controller
@RequestMapping(value="v15/{domain}/rating")
public class Rating15Controller {
    static final Logger LOG = LoggerFactory.getLogger(Rating15Controller.class);
    
    private RnrService rnrService;

    /**
     * Add a rating to a product.
     * @param productId domain-unique id for the product to rate
     * @param username optional. 
     * If authenticated, and RnrService.fallbackPrincipalName, 
     * principal.name will be used if username is null.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @param rating mandatory, the rating 0..100
     * @param comment options review comment
     * @return the new rating
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
        @RestCode(code=200, message="OK", description="Rating created")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public ResponseEntity<JRating> addRating(HttpServletRequest request,
                                             Principal principal,
                                             @RequestParam(required=true) String productId,
                                             @RequestParam(required=false) String username,
                                             @RequestParam(required=false) Float latitude,
                                             @RequestParam(required=false) Float longitude,
                                             @RequestParam int rating,
                                             @RequestParam(required=false) String comment) {

        final JRating body = rnrService.addRating(productId, username,
                null != principal ? principal.getName() : null, latitude, longitude, rating, comment);

        return new ResponseEntity<JRating>(body, HttpStatus.OK);
    }

    /**
     * Delete a rating with a specific id.
     * @param id The unique rating id
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="Rating deleted"),
            @RestCode(code=404, message="NOK", description="Rating not found and can not be deleted")
    })
    @RequestMapping(value="{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JRating> deleteLike(HttpServletRequest request,
                                            Principal principal,
                                            @PathVariable long id) {

        final JRating body = rnrService.deleteRating(id);

        if (null == body)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JRating>(HttpStatus.OK);
    }

    /**
     * Get rating details for a specific id.
     * @param id The unique like id
     * @return the rating details
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="Rating found"),
            @RestCode(code=404, message="NOK", description="Rating not found")
    })
    @RequestMapping(value="{id}", method= RequestMethod.GET)
    public ResponseEntity<JRating> getLike(HttpServletRequest request,
                                         Principal principal,
                                         @PathVariable long id) {

        final JRating body = rnrService.getRating(id);

        if (null == body)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JRating>(body, HttpStatus.OK);
    }

    /**
     * Returns all ratings done by a specific user.
     * @param username optional. 
     * If authenticated, and RnrService.fallbackPrincipalName, 
     * principal.name will be used if username is null.
     * @return a Collection of my JRatings
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
        @RestCode(code=200, message="OK", description="Ratings found for user")
    })
    @RequestMapping(value="my", method= RequestMethod.POST)
    public ResponseEntity<Collection<JRating>> getMyRatings(HttpServletRequest request,
                                                            Principal principal,
                                                            @RequestParam(required=false) String username) {

        try {
            final Collection<JRating> body = rnrService.getMyRatings(username, 
                    null != principal ? principal.getName() : null);

            return new ResponseEntity<Collection<JRating>>(body, HttpStatus.OK);
        }
        catch (IllegalArgumentException usernameNull) {
            return new ResponseEntity<Collection<JRating>>(HttpStatus.UNAUTHORIZED);
        }
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
