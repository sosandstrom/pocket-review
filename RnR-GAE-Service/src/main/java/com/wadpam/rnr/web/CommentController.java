package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DComment;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.json.JComment;
import com.wadpam.rnr.json.JLike;
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
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;

/**
 * The comment controller implements all REST methods related to commenting.
 * @author mlv
 */
@Controller
@RequestMapping(value="{domain}/comment")
public class CommentController {

    static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    private RnrService rnrService;

    /**
     * Add a comment to a product.
     * @param productId domain-unique id for the product to comment
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @param comment the comment
     * @return the new comment
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created comment")
    })
    @RequestMapping(value="", method= RequestMethod.PUT)
    public RedirectView addComment(HttpServletRequest request,
                                   Principal principal,
                                   @RequestParam(required=true) String productId,
                                   @RequestParam(required=false) String username,
                                   @RequestParam(required=false) Float latitude,
                                   @RequestParam(required=false) Float longitude,
                                   @RequestParam(required=true) String comment) {

        final DComment body = rnrService.addComment(productId, username,
                null != principal ? principal.getName() : null, latitude, longitude, comment);

        return new RedirectView(request.getRequestURI() + "/" + body.getId().toString());
    }

    /**
     * Delete a comment with a specific id.
     * @param id The unique comment id
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="Comment deleted"),
            @RestCode(code=404, message="NOK", description="Comment not found and can not be deleted")
    })
    @RequestMapping(value="{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JComment> deleteComment(HttpServletRequest request,
                                                  Principal principal,
                                                  @PathVariable long id) {

        final DComment body = rnrService.deleteComment(id);

        if (null == body)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JComment>(HttpStatus.OK);
    }

    /**
     * Get comment details for a specific id.
     * @param id The unique comment id
     * @return the comment details
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="Comment found"),
            @RestCode(code=404, message="NOK", description="Comment not found")
    })
    @RequestMapping(value="{id}", method= RequestMethod.GET)
    public ResponseEntity<JComment> getComment(HttpServletRequest request,
                                               Principal principal,
                                               @PathVariable long id) {

        final DComment body = rnrService.getComment(id);

        if (null == body)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JComment>(Converter.convert(body, request), HttpStatus.OK);
    }

    /**
     * Returns all comments done by a specific user.
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a list of comments
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="All comments for user")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="username")
    public ResponseEntity<Collection<JComment>> getMyComments(HttpServletRequest request,
                                                              Principal principal,
                                                              @RequestParam(required=false) String username) {

        try {
            final Collection<DComment> body = rnrService.getMyComments(username,
                    null != principal ? principal.getName() : null);

            return new ResponseEntity<Collection<JComment>>((Collection<JComment>)Converter.convert(body, request),
                    HttpStatus.OK);
        }
        catch (IllegalArgumentException usernameNull) {
            return new ResponseEntity<Collection<JComment>>(HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Returns all comments for a specific product.
     * @param productId the product to looks for
     * @return a list of comments
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="All comments for product")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="productId")
    public ResponseEntity<Collection<JComment>> getAllCommentsForProduct(HttpServletRequest request,
                                                    Principal principal,
                                                    @RequestParam(required=true) String productId) {

        final Collection<DComment> body = rnrService.getAllCommentsForProduct(productId);

        return new ResponseEntity<Collection<JComment>>((Collection<JComment>)Converter.convert(body, request),
                HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
