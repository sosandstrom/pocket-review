package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.analytics.google.GoogleAnalyticsTracker;
import com.wadpam.open.analytics.google.GoogleAnalyticsTrackerBuilder;
import com.wadpam.open.json.JCursorPage;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.json.JLike;
import com.wadpam.rnr.service.RnrService;
import com.wadpam.open.exceptions.NotFoundException;
import com.wadpam.open.web.AbstractRestController;
import net.sf.mardao.core.CursorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;


/**
 * The like controller implements all REST methods related to likes.
 * @author mattiaslevin
 */
@Controller
public class LikeController extends AbstractRestController {
    static final Logger LOG = LoggerFactory.getLogger(LikeController.class);

    public static final int ERR_BASE_LIKE = RnrService.ERR_BASE_LIKE;
    public static final int ERR_LIKE_NOT_FOUND = ERR_BASE_LIKE + 1;

    static final Converter CONVERTER = new Converter();

    private RnrService rnrService;

    /**
     * Add a like to a product.
     *
     * This method will either redirect to the created like or the product
     * summary depending on the incoming uri.
     * @param productId domain-unique id for the product to like
     * @param username optional. A unique user name or id.
     *                 Needed in order to perform user related operations later on.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @return redirect to the created like or product summary
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=302, message="OK", description="Redirect to the newly created like or product summary")
    })
    @RequestMapping(value={"{domain}/like", "{domain}/product/like"}, method= RequestMethod.POST)
    public RedirectView addLike(HttpServletRequest request,
                                HttpServletResponse response,
                                UriComponentsBuilder uriBuilder,
                                @ModelAttribute("trackingCode") String trackingCode,
                                @PathVariable String domain,
                                @RequestParam String productId,
                                @RequestParam(required=false) String username,
                                @RequestParam(required=false) Float latitude,
                                @RequestParam(required=false) Float longitude) {

        // Create a tracker if tracking code is set
        GoogleAnalyticsTracker tracker = null;
        if (null != trackingCode) {
            LOG.debug("Create tracker with tracking code:{}", trackingCode);
            tracker = new GoogleAnalyticsTrackerBuilder()
                    .withNameAndTrackingCode(domain, trackingCode)
                    .withDeviceFromRequest(request)
                    .withVisitorId(username != null ? username.hashCode() : "anonymous".hashCode())
                    .build();
        }

        final DLike body = rnrService.addLike(domain, productId, username, latitude, longitude, tracker);

        // Redirect to different urls depending on request uri
        String redirectUri;
        if (request.getRequestURI().contains("product")) {
            // Redirect to the product summary
            redirectUri = uriBuilder.path("/{domain}/product/{id}").
                    buildAndExpand(domain, productId).toUriString();
        } else {
            // Redirect to the like
            redirectUri = uriBuilder.path("/{domain}/like/{id}").
                    buildAndExpand(domain, body.getId()).toUriString();
        }

        return new RedirectView(redirectUri);
    }

    /**
     * Delete a like with a specific id.
     * @param id The unique like id
     * @return a http response code indicating the outcome of the operation
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="Like deleted"),
            @RestCode(code=404, message="NOK", description="Like not found and can not be deleted")
    })
    @RequestMapping(value="{domain}/like/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JLike> deleteLike(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable long id) {

        final DLike body = rnrService.deleteLike(id);
        if (null == body)
            throw new NotFoundException(ERR_LIKE_NOT_FOUND,
                    String.format("Like with id:%s not found", id));

        return new ResponseEntity<JLike>(HttpStatus.OK);
    }

    /**
     * Get like details for a specific id.
     * @param id The unique like id
     * @return the like details
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="Like found"),
            @RestCode(code=404, message="NOK", description="Like not found")
    })
    @RequestMapping(value="{domain}/like/{id}", method= RequestMethod.GET)
    public ResponseEntity<JLike> getLike(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable long id) {

        final DLike body = rnrService.getLike(id);
        if (null == body)
            throw new NotFoundException(ERR_LIKE_NOT_FOUND,
                    String.format("Like with id:%s not found", id));

        return new ResponseEntity<JLike>(CONVERTER.convert(body), HttpStatus.OK);
    }

    /**
     * Returns all likes done by a specific user.
     * @param username a unique user name or id
     * @return a list of my likes
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="All likes for user")
    })
    @RequestMapping(value="{domain}/like", method= RequestMethod.GET, params="username")
    public ResponseEntity<Collection<JLike>> getMyLikes(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @RequestParam String username) {

        final Iterable<DLike> dLikeIterable = rnrService.getMyLikes(username);

        return new ResponseEntity<Collection<JLike>>(
                (Collection<JLike>)CONVERTER.convert(dLikeIterable),
                HttpStatus.OK);
    }

    /**
     * Returns random likes.
     * If a username parameter is provider the users like (if any) will be
     * returned first in the list.
     *
     * Random likes are cached per product for 10 minutes. This means that
     * the likes will be the same during 10 minutes even if new are added.
     * @param productId the product id
     * @param username a unique user name or id
     * @param limit the number if random likes to return.
     *
     * @return a list of likes likes
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="All likes for user")
    })
    @RequestMapping(value="{domain}/like/random", method= RequestMethod.GET)
    public ResponseEntity<Collection<JLike>> getRandomLikes(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String productId,
            @RequestParam(required=false) String username,
            @RequestParam(required=false, defaultValue="5") int limit) {

        final Collection<DLike> randomLikes = rnrService.getRandomLikes(productId, username, limit);

        return new ResponseEntity<Collection<JLike>>(
                (Collection<JLike>)CONVERTER.convert(randomLikes),
                HttpStatus.OK);
    }

    /**
     * Returns all likes for a specific product.
     * @param productId the product to looks for
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a page of likes
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Page of likes for product")
    })
    @RequestMapping(value="{domain}/like", method= RequestMethod.GET, params="productId")
    public ResponseEntity<JCursorPage<JLike>> getAllLikesForProduct(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String productId,
            @RequestParam(defaultValue="10") int pagesize,
            @RequestParam(required=false) String cursor) {

        final CursorPage<DLike, Long> dPage = rnrService.getAllLikesForProduct(productId, pagesize, cursor);

        return new ResponseEntity<JCursorPage<JLike>>(
                (JCursorPage<JLike>)CONVERTER.convert(dPage),
                HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}
