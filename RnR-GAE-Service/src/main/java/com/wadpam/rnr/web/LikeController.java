package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.dao.DResultDao;
import com.wadpam.rnr.json.JResult;
import com.wadpam.rnr.service.LikeService;
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

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/24/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value="{domain}/like")
public class LikeController {

    static final Logger LOG = LoggerFactory.getLogger(LikeController.class);

    private LikeService likeService;


    /**
     * Adds a Like to a product.
     * @param productId domain-unique id for the product to like
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @return the new total number of Likes for this product
     */
    // TODO: What to return from this operation. A JResult with a new property - numberOfLikes
    @RestReturn(value=JResult.class, entity=JResult.class, code={
            @RestCode(code=200, message="OK", description="Like added to product")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.POST)
    public ResponseEntity<JResult> addLike(HttpServletRequest request,
                                             Principal principal,
                                             @PathVariable String productId,
                                             @RequestParam(required=false) String username,
                                             @RequestParam(required=false) Float latitude,
                                             @RequestParam(required=false) Float longitude) {

        final JResult body = likeService.addLike(productId, username,
                null != principal ? principal.getName() : null, latitude, longitude);

        return new ResponseEntity<JResult>(body, HttpStatus.OK);
    }


    /**
     * Returns the number of Likes for a product.
     * @param productId domain-unique id for the product
     * @return the total number of Likes for the specified productId
     */
    @RestReturn(value=JResult.class, entity=JResult.class, code={
            @RestCode(code=200, message="OK", description="Number of Likes found for product"),
            @RestCode(code=404, message="Not Found", description="Product not found")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.GET)
    public ResponseEntity<JResult> getNumberOfLikes(
            @PathVariable String productId) {

        final JResult body = likeService.getNumberOfLikes(productId);
        if (null == body) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<JResult>(body, HttpStatus.OK);
    }

    // Setters for Spring
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

}
