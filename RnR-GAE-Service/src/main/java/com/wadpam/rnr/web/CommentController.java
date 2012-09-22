package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DComment;
import com.wadpam.rnr.json.JComment;
import com.wadpam.rnr.service.RnrService;
import com.wadpam.server.web.AbstractRestController;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The comment controller implements all REST methods related to commenting.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/comment")
public class CommentController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    private RnrService rnrService;

    /**
     * Add a comment to a product.
     *
     * If you like to comment on a ratings, you can use the rating id as the product id.
     * If you need the ability to comment on both ratings and products you can either use
     * two different domains or prefix the productId depending on the type of comment,
     * e.g. PROD-3384, RATING-34
     * @param productId domain-unique id for the product to comment
     * @param parentId optional. A parent comment used to create nested comments
     * @param username optional. A unique user name or id.
     *                 Needed in order to perform user related operations later on.
     * @param latitude optional. -90..90
     * @param longitude optional -180..180
     * @param comment the comment
     * @return the new comment
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created comment")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public RedirectView addComment(HttpServletRequest request,
                                   Principal principal,
                                   @RequestParam(required=true) String productId,
                                   @RequestParam(required=true) Long parentId,
                                   @RequestParam(required=false) String username,
                                   @RequestParam(required=false) Float latitude,
                                   @RequestParam(required=false) Float longitude,
                                   @RequestParam(required=true) String comment) {

        final DComment body = rnrService.addComment(productId, parentId, username, latitude, longitude, comment);

        return new RedirectView(request.getRequestURI() + "/" + body.getId().toString());
    }

    /**
     * Delete a comment with a specific id.
     * @param id The unique comment id
     * @return a http response code indicating the outcome of the operation
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

        return new ResponseEntity<JComment>(Converter.convert(body), HttpStatus.OK);
    }

    /**
     * Returns all comments done by a specific user.
     * @param username a unique user name or id
     * @return a list of comments
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="All comments for user")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="username")
    public ResponseEntity<Collection<JComment>> getMyComments(HttpServletRequest request,
                                                              Principal principal,
                                                              @RequestParam(required=true) String username) {

        final Collection<DComment> body = rnrService.getMyComments(username);

        return new ResponseEntity<Collection<JComment>>((Collection<JComment>)Converter.convert(body), HttpStatus.OK);
    }

    /**
     * Returns all comments for a specific product.
     * @param productId the product to look for
     * @param hierarchy optional. Decide if the returned list should be returned nested.
     *                  This is useful is comment contain parent relations to create nested comments.
     *                  Default is false. Will add some overhead.
     * @return a list of comments
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="All comments for product")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="productId")
    public ResponseEntity<Collection<JComment>> getAllCommentsForProduct(HttpServletRequest request,
                                                    Principal principal,
                                                    @RequestParam(required=true) String productId,
                                                    @RequestParam(required=false, defaultValue="false") boolean hierarchy) {

        final Collection<DComment> dComments = rnrService.getAllCommentsForProduct(productId);

        Collection<JComment> jComments = null;
        if (hierarchy == false)
            jComments =  (Collection<JComment>)Converter.convert(dComments);
        else {
            LOG.debug("Arrange the comments in hierarchy based on parent comments");

            jComments = new ArrayList<JComment>();
            Map<Long, Collection<JComment>> remainingComments = new HashMap<Long, Collection<JComment>>();

            // Split in root and non-root comments
            for (DComment dComment : dComments) {
                // Convert to JComment before we do anything
                JComment jComment = Converter.convert(dComment);

                if (null == jComment.getParentId())
                    jComments.add(jComment);
                else {
                    Collection<JComment> children = remainingComments.get(jComment.getParentId());
                    if (null == children) {
                        children = new ArrayList<JComment>();
                        remainingComments.put(jComment.getParentId(), children);
                    }
                    children.add(jComment);
                }
            }

            // Recursively add all children
            for (JComment rootComment : jComments)
                addChildren(rootComment, remainingComments);
        }

        return new ResponseEntity<Collection<JComment>>(jComments, HttpStatus.OK);
    }

    // Build a hierarchy of comments
    private void addChildren(JComment parentComment, Map<Long, Collection<JComment>> remainingComments) {
        Collection<JComment> children = remainingComments.get(parentComment.getId());

        if (null == children)
            // No children. Reached a leaf, do nothing
            return;

        // Add children
        parentComment.setChildren(children);

        // Recursively build the hierarchy
        for (JComment child : children)
            addChildren(child, remainingComments);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
