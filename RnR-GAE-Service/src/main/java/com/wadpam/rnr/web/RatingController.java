package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.json.JCursorPage;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.json.JHistogram;
import com.wadpam.rnr.json.JRating;
import com.wadpam.rnr.service.RnrService;
import com.wadpam.open.exceptions.NotFoundException;
import com.wadpam.open.web.AbstractRestController;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;


/**
 * The rating controller implements all REST methods related to ratings and reviews.
 * @author os
 */
@Controller
public class RatingController extends AbstractRestController {
    static final Logger LOG = LoggerFactory.getLogger(RatingController.class);

    public static final int ERR_BASE_RATE = RnrService.ERR_BASE_RATE;
    public static final int ERR_RATE_NOT_FOUND = ERR_BASE_RATE + 1;

    static final Converter CONVERTER = new Converter();

    private RnrService rnrService;


    /**
     * Add a rating to a product.
     *
     * This method will either redirect to the created rating or the product
     * summary depending on the incoming uri.
     * @param productId domain-unique id for the product to rate
     * @param username optional. A unique user name or id.
     *                 Needed in order to perform user related operations later on.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @param rating mandatory, the rating 0..100
     * @param comment optional. review comment
     * @return redirect to the create rating or product summary
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
        @RestCode(code=302, message="OK", description="Redirect to newly created rating or product summary")
    })
    @RequestMapping(value={"{domain}/rating", "{domain}/product/rating"}, method= RequestMethod.POST)
    public RedirectView addRating(HttpServletRequest request,
                                  HttpServletResponse response,
                                  UriComponentsBuilder uriBuilder,
                                  @PathVariable String domain,
                                  @RequestParam String productId,
                                  @RequestParam int rating,
                                  @RequestParam(required=false) String username,
                                  @RequestParam(required=false) Float latitude,
                                  @RequestParam(required=false) Float longitude,
                                  @RequestParam(required=false) String comment) {

        final DRating body = rnrService.addRating(domain, productId, username,
                latitude, longitude, rating, comment);

        // Redirect to different urls depending on request uri
        String redirectUri;
        if (request.getRequestURI().contains("product")) {
            // Redirect to the product summary
            redirectUri = uriBuilder.path("/{domain}/product/{id}").
                    buildAndExpand(domain, productId).toUriString();
        } else {
            // Redirect to the rating
            redirectUri = uriBuilder.path("/{domain}/rating/{id}").
                    buildAndExpand(domain, body.getId()).toUriString();
        }

        return new RedirectView(redirectUri);
    }

    /**
     * Delete a rating with a specific id.
     * @param id The unique rating id
     * @return a http response code indicating the outcome of the operation
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="Rating deleted"),
            @RestCode(code=404, message="NOK", description="Rating not found and can not be deleted")
    })
    @RequestMapping(value="{domain}/rating/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JRating> deleteLike(HttpServletRequest request,
                                              HttpServletResponse response,
                                              @PathVariable long id) {

        final DRating body = rnrService.deleteRating(id);
        if (null == body)
            throw new NotFoundException(ERR_RATE_NOT_FOUND,
                    String.format("Rating with id:%s not found", id));

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
    @RequestMapping(value="{domain}/rating/{id}", method= RequestMethod.GET)
    public ResponseEntity<JRating> getLike(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @PathVariable long id) {

        final DRating body = rnrService.getRating(id);
        if (null == body)
            throw new NotFoundException(ERR_RATE_NOT_FOUND,
                    String.format("Rating with id:%s not found", id));

        return new ResponseEntity<JRating>(CONVERTER.convert(body), HttpStatus.OK);
    }

    /**
     * Return all ratings done by a specific user.
     * @param username a unique user name or id
     * @return a list of ratings
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
        @RestCode(code=200, message="OK", description="Ratings found for user")
    })
    @RequestMapping(value="{domain}/rating", method= RequestMethod.GET, params="username")
    public ResponseEntity<Collection<JRating>> getMyRatings(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            @RequestParam String username) {

        final Iterable<DRating> dRatingIterable = rnrService.getMyRatings(username);

        return new ResponseEntity<Collection<JRating>>(
                (Collection<JRating>)CONVERTER.convert(dRatingIterable),
                HttpStatus.OK);
    }

    /**
     * Returns all ratings for a specific product.
     * @param productId the product to looks for
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a list of ratings
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Page of ratings for product")
    })
    @RequestMapping(value="{domain}/rating", method= RequestMethod.GET, params="productId")
    public ResponseEntity<JCursorPage<JRating>> getAllRatingsForProduct(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String productId,
            @RequestParam(defaultValue="10") int pagesize,
            @RequestParam(required=false) String cursor) {

        final CursorPage<DRating, Long> dPage = rnrService.getAllRatingsForProduct(productId, pagesize, cursor);

        return new ResponseEntity<JCursorPage<JRating>>(
                (JCursorPage<JRating>)CONVERTER.convert(dPage),
                HttpStatus.OK);
    }

    /**
     * Returns a histogram over all ratings for a specific product.
     * @param productId the product to looks for
     * @param interval Optional. the interval in the returned histogram. Default 10.
     * @return a histogram
     */
    @RestReturn(value=JHistogram.class, entity=JHistogram.class, code={
            @RestCode(code=200, message="OK", description="Histogram for product")
    })
    @RequestMapping(value="{domain}/rating/histogram", method= RequestMethod.GET, params="productId")
    public ResponseEntity<JHistogram> getHistogramForProduct(HttpServletRequest request,
                                                             HttpServletResponse response,
                                                             @RequestParam String productId,
                                                             @RequestParam(defaultValue="10") int interval) {

        final Map<Long, Long> values = rnrService.getHistogramForProduct(productId, interval);

        JHistogram histogram = new JHistogram();
        histogram.setInterval(interval);
        histogram.setHistogram(values);

        return new ResponseEntity<JHistogram>(histogram, HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}
