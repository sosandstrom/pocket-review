package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.json.JRating;
import com.wadpam.rnr.json.JResult;
import com.wadpam.rnr.json.JResultPage;
import com.wadpam.rnr.service.RnrService;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


// TODO: Rename product to item?
// TODO: Update the name of the REST path, see below
/*
 /api/{domain}/{itemId}/rating (POST rate an item, GET return all ratings for item)
 /api/{domain}/{itemId}/review (review am otem)
 /api/{domain}/{itemId}/like (like an item)
 /api/{domain}/{itemId}/favorite (make it a favorite for the user)

 /api/{domain}/my/rating (return my ratings)
 /api/{domain}/my/review
 /api/{domain}/my/like

  Or we can keep the existing but I think we should one single method for getting all item info, see below

 /api/[domain}/item (get item info for a list of items, if list is empty get all)
 /api/{domain}/item/{itemId} (get all item information (rating, likes, reviews etc)
 /api/{domain}/item/nearby (get nearby items with different sorting options)
 /api/{domain}/item/favorites (get user favorites)
 */
// TODO: Review the naming of the names on JResult
/*
ratingAverage
ratingTotalSum
ratingCount
ratings (URL to the REST returning all ratings for the item)
likeCount
likes (URL)
reviewCount
reviews (URL)
 */
// TODO: Rename JResut to something else, JItem, JItemInfo?
// TODO: Updating JResult is not thread safe. Do the update in a serial task queue
// TODO: Move location from DRating to DResult


/**
 * The rating controller implements all REST methods related to ratings and reviews
 * @author os
 */
@Controller
@RequestMapping(value="{domain}/rating")
public class RatingController {
    static final Logger LOG = LoggerFactory.getLogger(RatingController.class);
    
    private RnrService rnrService;

    /**
     * Adds a rating to a product.
     * @param productId domain-unique id for the product to rate
     * @param username optional. 
     * If authenticated, and RnrService.fallbackPrincipalName, 
     * principal.name will be used if username is null.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @param rating mandatory, the rating 0..100
     * @return the new average rating for this product
     */
    @RestReturn(value=JResult.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Rating added to product")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.POST)
    public ResponseEntity<JResult> addRating(HttpServletRequest request,
            Principal principal,
            @PathVariable String productId,
            @RequestParam(required=false) String username,
            @RequestParam(required=false) Float latitude,
            @RequestParam(required=false) Float longitude,
            @RequestParam int rating) {
        final JResult body = rnrService.addRating(productId, username, 
                null != principal ? principal.getName() : null,
                latitude, longitude, rating);
        return new ResponseEntity<JResult>(body, HttpStatus.OK);
    }
    
    /**
     * Returns a list or average ratings for nearby products.
     *
     * If not latitude or longitude is provided in the request position provided by Google App Engine will be used.
     * @param latitude optional, the latitude to search around
     * @param longitude optional, the longitude to search around
     * @param bits optional, the size of the bounding box to search within. Default is 15 for a 1224m box.
     * @return a Collection of JRatings
     */
    // TODO: Add max number of hits to return
    @RestReturn(value=JResult.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Nearby average ratings found")
    })
    @RequestMapping(value="nearby", method= RequestMethod.GET)
    public ResponseEntity<Collection<JResult>> findNearbyRatings(HttpServletRequest request,
            @RequestParam(required=false) Float latitude,
            @RequestParam(required=false) Float longitude,
            @RequestParam(defaultValue="15") int bits) {
        if (null == latitude) {
            final String cityLatLong = request.getHeader("X-AppEngine-CityLatLong");
            if (null != cityLatLong) {
                final int index = cityLatLong.indexOf(',');
                latitude = Float.parseFloat(cityLatLong.substring(0, index));
                longitude = Float.parseFloat(cityLatLong.substring(index+1));
            }
        }
        
        final Collection<JResult> body = rnrService.findNearbyRatings(
                latitude, longitude, bits);
        return new ResponseEntity<Collection<JResult>>(body, HttpStatus.OK);
    }

    /**
     * Returns a list or average ratings for nearby products in KML format.
     *
     * If not latitude or longitude is provided in the request position provided by Google App Engines will be used.
     * @param latitude optional, the latitude to search around
     * @param longitude optional, the longitude to search around
     * @param bits optional, the size of the bounding box to search within. Default is 15 for a 1224m box.
     * @return a KML of JRatings
     */
    // TODO: Add max number of hits to return
    @RestReturn(value=JRating.class, entity=JRating.class, code={
        @RestCode(code=200, message="OK", description="Nearby average ratings found")
    })
    @RequestMapping(value="nearby.kml", method= RequestMethod.GET)
    public void findNearbyRatingsKml(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required=false) Float latitude,
            @RequestParam(required=false) Float longitude,
            @RequestParam(defaultValue="15") int bits) throws IOException {
        if (null == latitude) {
            final String cityLatLong = request.getHeader("X-AppEngine-CityLatLong");
            if (null != cityLatLong) {
                final int index = cityLatLong.indexOf(',');
                latitude = Float.parseFloat(cityLatLong.substring(0, index));
                longitude = Float.parseFloat(cityLatLong.substring(index+1));
            }
        }
        
        response.setContentType("application/vnd.google-earth.kml+xml");
        final PrintWriter out = response.getWriter();
        rnrService.nearbyRatingsKml(
                latitude, longitude, bits, out);
        out.flush();
        out.close();
    }

    /**
     * Returns the average rating for a product.
     * @param productId domain-unique id for the product
     * @return the average rating for specified productId
     */
    @RestReturn(value=JResult.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Average rating found for product"),
        @RestCode(code=404, message="Not Found", description="Product not found")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.GET)
    public ResponseEntity<JResult> getAverageRating(
            @PathVariable String productId) {
        final JResult body = rnrService.getAverage(productId);
        if (null == body) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<JResult>(body, HttpStatus.OK);
    }

    /**
     * Returns the average rating for all products.
     * @return a list of average rating for all products
     */
    @RestReturn(value=JResultPage.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Average ratings"),
        @RestCode(code=404, message="Not Found", description="Products not found")
    })
    @RequestMapping(value="", method= RequestMethod.GET)
    public ResponseEntity<JResultPage> getAverageRatings(
            @RequestParam(required=false) String cursor,
            @RequestParam(defaultValue="0") long offset,
            @RequestParam(defaultValue="10") long limit
            ) {
        final JResultPage body = rnrService.getAveragePage(cursor, offset, limit);
        return new ResponseEntity<JResultPage>(body, HttpStatus.OK);
    }

    /**
     * Returns the average rating for a list of products.
     * @param ids a list of productIds
     * @return a list of average rating for specified products
     */
    // TODO: How to document the ids parameter?
    @RestReturn(value=JResultPage.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Average ratings found for products")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="ids")
    public ResponseEntity<Collection<JResult>> getAverageRatings(
            @RequestParam(value="ids") String ids[]) {
        final Collection<JResult> body = rnrService.getAverageRatings(ids);
        return new ResponseEntity<Collection<JResult>>(body, HttpStatus.OK);
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
    // TODO: I think there might be a bug. The same rating is returned more then once. Maybe because I managed to rate more then once
    @RequestMapping(value="me", method= RequestMethod.GET)
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

    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
