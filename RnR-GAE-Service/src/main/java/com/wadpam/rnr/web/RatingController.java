package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.json.JProduct;
import com.wadpam.rnr.json.JProductV15;
import com.wadpam.rnr.json.JRating;
import com.wadpam.rnr.service.RnrService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
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
 * The rating controller implements all REST methods related to ratings and reviews
 *
 * All of the methods supported by this controller is deprecated. Developers should
 * use the REST end-points provided by Rating15Controller instead. This controller is
 * still support in order to provide backwards compatibility towards existing apps.
 *
 * @author os
 */
@Controller
@RequestMapping(value="{domain}/rating")
public class RatingController {
    static final Logger LOG = LoggerFactory.getLogger(RatingController.class);
    
    private RnrService rnrService;

    /**
     * Add a rating to a product.
     *
     * This method has been deprecated in favour of the /v15/{domain}/rating/{productId} method.
     *
     * @param productId domain-unique id for the product to rate
     * @param username optional. 
     * If authenticated, and RnrService.fallbackPrincipalName, 
     * principal.name will be used if username is null.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @param rating mandatory, the rating 0..100
     * @return the new result for this product
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
        @RestCode(code=200, message="OK", description="Rating added to product")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.POST)
    @Deprecated
    public ResponseEntity<JProduct> addRating(HttpServletRequest request,
                                              Principal principal,
                                              @PathVariable String productId,
                                              @RequestParam(required=false) String username,
                                              @RequestParam(required=false) Float latitude,
                                              @RequestParam(required=false) Float longitude,
                                              @RequestParam int rating) {

        final JRating jRating = rnrService.addRating(productId, username,
                null != principal ? principal.getName() : null, latitude, longitude, rating, "");

        // Get a product from the rating, needed to retain backwards compatibility
        final JProduct body = convertFromV15(rnrService.getProduct(jRating.getProductId()));

        return new ResponseEntity<JProduct>(body, HttpStatus.OK);
    }

    /**
     * Returns the product summary, including the average rating.
     *
     * This method has been deprecated in favour of the /{domain}/product/{productId} method.
     *
     * @param productId domain-unique id for the product
     * @return the average rating for specified productId
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Product summary found"),
            @RestCode(code=404, message="Not Found", description="Product not found")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.GET)
    @Deprecated
    public ResponseEntity<JProduct> getAverageRating(
            @PathVariable String productId) {
        final JProduct body = convertFromV15(rnrService.getProduct(productId));
        if (null == body) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<JProduct>(body, HttpStatus.OK);
    }

    /**
     * Returns the average rating for a list of products.
     *
     * This method has been deprecated in favour of the /{domain}/product/{productId} method.
     *
     * @param ids a list of productIds
     * @return a list of average rating for specified products
     */
    // TODO: How to document the ids parameter?
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Product summaries not found")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="ids")
    @Deprecated
    public ResponseEntity<Collection<JProduct>> getAverageRatings(
            @RequestParam(value="ids") String ids[]) {
        final Collection<JProduct> body = convertFromV15(rnrService.getProducts(ids));
        return new ResponseEntity<Collection<JProduct>>(body, HttpStatus.OK);
    }


    // Convert from V15 to v10 Json results. Needed to retain backwards compatibility
    private static JProduct convertFromV15(JProductV15 resultV15) {
        JProduct resultV10 = new JProduct();

        resultV10.setProductId(resultV15.getId());
        resultV10.setLocation(resultV15.getLocation());
        resultV10.setRatingCount(resultV15.getRatingCount());
        resultV10.setRatingSum(resultV15.getRatingSum());

        return resultV10;
    }

    // Convert array
    private static Collection<JProduct> convertFromV15(Collection<JProductV15> resultV15List) {
        Collection<JProduct> resultV10List = new ArrayList<JProduct>(resultV15List.size());

        for (JProductV15 resultV15 : resultV15List)  {
            resultV10List.add(convertFromV15(resultV15));
        }

        return resultV10List;
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
