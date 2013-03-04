package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.analytics.google.GoogleAnalyticsTracker;
import com.wadpam.open.analytics.google.GoogleAnalyticsTrackerBuilder;
import com.wadpam.open.json.JCursorPage;
import com.wadpam.rnr.domain.DThumbs;
import com.wadpam.rnr.json.JThumbs;
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
 * A Thumbs and and down controller
 * @author mattiaslevin
 */
@Controller
public class ThumbsController extends AbstractRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ThumbsController.class);

    private static final int ERR_BASE_THUMBS = RnrService.ERR_BASE_THUMBS;
    private static final int ERR_THUMBS_NOT_FOUND = ERR_BASE_THUMBS + 1;

    private static final Converter CONVERTER = new Converter();

    private RnrService rnrService;


    /**
     * Add a thumbs up.
     *
     * This method will either redirect to the created thumbs up or the product
     * summary depending on the incoming uri.
     * @param productId domain-unique id for the product to rate
     * @param username optional. A unique user name or id.
     *                 Needed in order to perform user related operations later on.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @return redirect to the create thumbs up or product summary
     */
    @RestReturn(value=JThumbs.class, entity=JThumbs.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created thumbs up or product summary")
    })
    @RequestMapping(value={"{domain}/thumbs/up", "{domain}/product/thumbs/up"}, method= RequestMethod.POST)
    public RedirectView addThumbsUp(HttpServletRequest request,
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

        final DThumbs body = rnrService.addThumbs(domain, productId, username,
                latitude, longitude, RnrService.Thumbs.UP, tracker);

        // Redirect to different urls depending on request uri
        String redirectUri;
        if (request.getRequestURI().contains("product")) {
            // Redirect to the product summary
            redirectUri = uriBuilder.path("/{domain}/product/{id}").
                    buildAndExpand(domain, productId).toUriString();
        } else {
            // Redirect to the thumbs
            redirectUri = uriBuilder.path("/{domain}/thumbs/{id}").
                    buildAndExpand(domain, body.getId()).toUriString();
        }

        return new RedirectView(redirectUri);
    }

    /**
     * Add a thumbs down.
     *
     * This method will either redirect to the created thumbs down or the product
     * summary depending on the incoming uri.
     * @param productId domain-unique id for the product to rate
     * @param username optional. A unique user name or id.
     *                 Needed in order to perform user related operations later on.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @return redirect to the create thumbs down or product summary
     */
    @RestReturn(value=JThumbs.class, entity=JThumbs.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created thumbs down or product summary")
    })
    @RequestMapping(value={"{domain}/thumbs/down", "{domain}/product/thumbs/down"}, method= RequestMethod.POST)
    public RedirectView addThumbsDown(HttpServletRequest request,
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

        final DThumbs body = rnrService.addThumbs(domain, productId, username,
                latitude, longitude, RnrService.Thumbs.DOWN, tracker);

        // Redirect to different urls depending on request uri
        String redirectUri;
        if (request.getRequestURI().contains("product")) {
            // Redirect to the product summary
            redirectUri = uriBuilder.path("/{domain}/product/{id}").
                    buildAndExpand(domain, productId).toUriString();
        } else {
            // Redirect to the thumbs
            redirectUri = uriBuilder.path("/{domain}/thumbs/{id}").
                    buildAndExpand(domain, body.getId()).toUriString();
        }

        return new RedirectView(redirectUri);
    }


    /**
     * Delete a thumbs with a specific id.
     * @param id the unique thumbs id
     * @return a http response code indicating the outcome of the operation
     */
    @RestReturn(value=JThumbs.class, entity=JThumbs.class, code={
            @RestCode(code=200, message="OK", description="Thumbs deleted"),
            @RestCode(code=404, message="NOK", description="Thumbs not found and can not be deleted")
    })
    @RequestMapping(value="{domain}/thumbs/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JThumbs> deleteThumbs(HttpServletRequest request,
                                                HttpServletResponse response,
                                                @PathVariable long id) {

        final DThumbs body = rnrService.deleteThumbs(id);
        if (null == body)
            throw new NotFoundException(ERR_THUMBS_NOT_FOUND,
                    String.format("Thumb with id:%s not found", id));

        return new ResponseEntity<JThumbs>(HttpStatus.OK);
    }

    /**
     * Get thumbs details for a specific id.
     * @param id the unique thumbs id
     * @return the thumbs details
     */
    @RestReturn(value=JThumbs.class, entity=JThumbs.class, code={
            @RestCode(code=200, message="OK", description="Thumbs found"),
            @RestCode(code=404, message="NOK", description="Thumbs not found")
    })
    @RequestMapping(value="{domain}/thumbs/{id}", method= RequestMethod.GET)
    public ResponseEntity<JThumbs> getThumbs(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @PathVariable long id) {

        final DThumbs body = rnrService.getThumbs(id);
        if (null == body)
            throw new NotFoundException(ERR_THUMBS_NOT_FOUND,
                    String.format("Thumb with id:%s not found", id));

        return new ResponseEntity<JThumbs>(CONVERTER.convert(body), HttpStatus.OK);
    }


    /**
     * Returns all thumbs up and down done by a specific user.
     * @param username a unique user name or id
     * @return a list of thumbs
     */
    @RestReturn(value=JThumbs.class, entity=JThumbs.class, code={
            @RestCode(code=200, message="OK", description="All thumbs for user")
    })
    @RequestMapping(value="{domain}/thumbs", method= RequestMethod.GET, params="username")
    public ResponseEntity<Collection<JThumbs>> getMyThumbs(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           @RequestParam String username) {

        final Iterable<DThumbs> dThumbsIterable = rnrService.getMyThumbs(username);

        return new ResponseEntity<Collection<JThumbs>>(
                (Collection<JThumbs>)CONVERTER.convert(dThumbsIterable),
                HttpStatus.OK);
    }

    /**
     * Returns all thumbs up and down for a specific product.
     * @param productId the product to looks for
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a page of thumbs
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="A page of thumbs for product")
    })
    @RequestMapping(value="{domain}/thumbs", method= RequestMethod.GET, params="productId")
    public ResponseEntity<JCursorPage<JThumbs>> getAllThumbsForProduct(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String productId,
            @RequestParam(defaultValue="10") int pagesize,
            @RequestParam(required=false) String cursor) {

        final CursorPage<DThumbs, Long> dPage = rnrService.getAllThumbsForProduct(productId, pagesize, cursor);

        return new ResponseEntity<JCursorPage<JThumbs>>(
                (JCursorPage<JThumbs>)CONVERTER.convert(dPage),
                HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
