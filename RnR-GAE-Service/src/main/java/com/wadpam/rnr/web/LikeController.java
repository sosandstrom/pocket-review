package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.json.JCursorPage;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.json.JLike;
import com.wadpam.rnr.service.RnrService;
import com.wadpam.server.web.AbstractRestController;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;


/**
 * The like controller implements all REST methods related to likes.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/like")
public class LikeController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(LikeController.class);
    static final Converter CONVERTER = new Converter();

    private RnrService rnrService;


    /**
     * Add a like to a product.
     * @param productId domain-unique id for the product to like
     * @param username optional. A unique user name or id.
     *                 Needed in order to perform user related operations later on.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @return the newly create like
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=302, message="OK", description="Redirect to the newly created like")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public RedirectView addLike(HttpServletRequest request,
                                HttpServletResponse response,
                                @PathVariable String domain,
                                @RequestParam(required=true) String productId,
                                @RequestParam(required=false) String username,
                                @RequestParam(required=false) Float latitude,
                                @RequestParam(required=false) Float longitude) {

        final DLike body = rnrService.addLike(domain, productId, username, latitude, longitude);

        return new RedirectView(request.getRequestURI() + "/" + body.getId().toString());
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
    @RequestMapping(value="{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JLike> deleteLike(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable long id) {

        rnrService.deleteLike(id);

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
    @RequestMapping(value="{id}", method= RequestMethod.GET)
    public ResponseEntity<JLike> getLike(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable long id) {

        final DLike body = rnrService.getLike(id);

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
    @RequestMapping(value="", method= RequestMethod.GET, params="username")
    public ResponseEntity<Collection<JLike>> getMyLikes(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @RequestParam(required=true) String username) {

        final Iterable<DLike> dLikeIterable = rnrService.getMyLikes(username);

        return new ResponseEntity<Collection<JLike>>((Collection<JLike>)CONVERTER.convert(dLikeIterable), HttpStatus.OK);
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
    @RequestMapping(value="", method= RequestMethod.GET, params="productId")
    public ResponseEntity<JCursorPage<JLike>> getAllLikesForProduct(HttpServletRequest request,
                                                                    HttpServletResponse response,
                                                                    @RequestParam(required=true) String productId,
                                                                    @RequestParam(defaultValue="10") int pagesize,
                                                                    @RequestParam(required=false) String cursor) {

        final CursorPage<DLike, Long> dPage = rnrService.getAllLikesForProduct(productId, pagesize, cursor);

        JCursorPage<JLike> cursorPage = new JCursorPage<JLike>();
        cursorPage.setCursor(dPage.getCursorKey().toString());
        cursorPage.setPageSize((long)pagesize);
        cursorPage.setItems((Collection<JLike>)CONVERTER.convert(dPage.getItems()));

        return new ResponseEntity<JCursorPage<JLike>>(cursorPage, HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}
