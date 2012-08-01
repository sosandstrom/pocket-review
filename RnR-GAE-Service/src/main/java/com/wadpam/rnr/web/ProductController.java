package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.json.JLike;
import com.wadpam.rnr.json.JProductPage;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/26/12
 * Time: 9:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value="{domain}/product")
public class ProductController {
    static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private RnrService rnrService;


    /**
     * Get a specific product.
     * @param productId domain-unique id for the product
     * @return the product
     */
    @RestReturn(value=JProductV15.class, entity=JProductV15.class, code={
            @RestCode(code=200, message="OK", description="Product found"),
            @RestCode(code=404, message="Not Found", description="Product not found")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.GET)
    public ResponseEntity<JProductV15> getProductInfo(HttpServletRequest request,
                                                      @PathVariable String productId) {

        final JProductV15 body = rnrService.getProduct(productId);

        if (null == body) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<JProductV15>(body, HttpStatus.OK);
        }
    }

    /**
     * Get a list of products.
     * @param ids a list of productIds
     * @return a list of products
     */
    // TODO: How to document the ids parameter?
    @RestReturn(value=JProductPage.class, entity=JProductV15.class, code={
            @RestCode(code=200, message="OK", description="Products found")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="ids")
    public ResponseEntity<Collection<JProductV15>> getProducts(HttpServletRequest request,
                                                               @RequestParam(value = "ids") String ids[]) {

        final Collection<JProductV15> body = rnrService.getProducts(ids);

        return new ResponseEntity<Collection<JProductV15>>(body, HttpStatus.OK);
    }

    /**
     * Get all products.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a list of products and new cursor.
     */
    @RestReturn(value=JProductPage.class, entity=JProductV15.class, code={
            @RestCode(code=200, message="OK", description="Products found"),
            @RestCode(code=404, message="Not Found", description="No products founds")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public ResponseEntity<JProductPage> getAllProducts(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "10") int pagesize,
                                                       @RequestParam(required = false) String cursor) {

        // Just in case
        if (null != cursor && cursor.isEmpty()) cursor = null;

        final JProductPage body = rnrService.getProductPage(cursor, pagesize);

        if (null == body)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JProductPage>(body, HttpStatus.OK);
    }

    /**
     * Returns a list of nearby products.
     *
     * If no latitude or longitude is provided in the request position provided by Google App Engine will be used.
     * @param latitude optional, the latitude to search around
     * @param longitude optional, the longitude to search around
     * @param bits optional, the size of the bounding box to search within. Default is 15 for a 1224m box.
     * @param sort optional, the sort order of the returned results
     *             0 - average rating, default value
     *             1 - number of likes
     * @param limit optional, the maximum number of results to return. Default 10
     * @return a list of products
     */
    @RestReturn(value=JProductV15.class, entity=JProductV15.class, code={
            @RestCode(code=200, message="OK", description="Nearby products found")
    })
    @RequestMapping(value="nearby", method= RequestMethod.GET)
    public ResponseEntity<Collection<JProductV15>> findNearbyProducts(HttpServletRequest request,
                                                                 @RequestParam(required=false) Float latitude,
                                                                 @RequestParam(required=false) Float longitude,
                                                                 @RequestParam(defaultValue="15") int bits,
                                                                 @RequestParam(defaultValue="0") int sort,
                                                                 @RequestParam(defaultValue="10") int limit)   {
        if (null == latitude) {
            final String cityLatLong = request.getHeader("X-AppEngine-CityLatLong");
            if (null != cityLatLong) {
                final int index = cityLatLong.indexOf(',');
                latitude = Float.parseFloat(cityLatLong.substring(0, index));
                longitude = Float.parseFloat(cityLatLong.substring(index+1));
            }
        }

        final Collection<JProductV15> body = rnrService.findNearbyProducts(latitude, longitude, bits, sort, limit);

        return new ResponseEntity<Collection<JProductV15>>(body, HttpStatus.OK);
    }

    /**
     * Returns a list of nearby products in KML format.
     *
     * If no latitude or longitude is provided in the request position provided by Google App Engines will be used.
     * @param latitude optional, the latitude to search around
     * @param longitude optional, the longitude to search around
     * @param bits optional, the size of the bounding box to search within. Default is 15 for a 1224m box.
     * @param sort optional, the sort order of the returned results
     *             0 - average rating, default value
     *             1 - number fo likes
     * @param limit optional, the maximum number of results to return. Default 10
     * @return a KML of JRatings
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="Nearby average ratings found")
    })
    @RequestMapping(value="nearby.kml", method= RequestMethod.GET)
    public void findNearbyProductsKml(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam(required = false) Float latitude,
                                      @RequestParam(required = false) Float longitude,
                                      @RequestParam(defaultValue = "15") int bits,
                                      @RequestParam(defaultValue = "0") int sort,
                                      @RequestParam(defaultValue = "10") int limit) throws IOException {
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
        rnrService.findNearbyProductsKml(latitude, longitude, bits, sort, limit, out);
        out.flush();
        out.close();
    }

    /**
     * Get most liked products.
     * @param limit Optional. The number of products to return. Default value 10.
     * @return a list of products sorted in most liked order
     */
    @RestReturn(value=JProductV15.class, entity=JProductV15.class, code={
            @RestCode(code=200, message="OK", description="Product found"),
            @RestCode(code=404, message="NOK", description="Product not found")
    })
    @RequestMapping(value="mostLiked", method= RequestMethod.GET)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JProductV15>> getMostLikedProducts(HttpServletRequest request,
                                                                   @RequestParam(defaultValue = "10") int limit) {

        final Collection<JProductV15> body = rnrService.getMostLikedProducts(limit);

        if (body.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Collection<JProductV15>>(body, HttpStatus.OK);
    }

    /**
     * Get all likes for a product.
     * @param productId domain-unique id for the product
     * @return a list of likes for the product
     */
    @RestReturn(value=JLike.class, entity=JLike.class, code={
            @RestCode(code=200, message="OK", description="Product found"),
            @RestCode(code=404, message="NOK", description="Product not found")
    })
    @RequestMapping(value="{productId}/likes", method= RequestMethod.GET)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JLike>> getAllLikesForProduct(HttpServletRequest request,
                                                                   Principal principal,
                                                                   @PathVariable String productId) {

        final Collection<JLike> body = rnrService.getAllLikesForProduct(productId,
                null != principal ? principal.getName() : null);

        if (body.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Collection<JLike>>(body, HttpStatus.OK);
    }

    /**
     * Get all ratings for a product.
     * @param productId domain-unique id for the product
     * @return a list of ratings for the product
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="Product found"),
            @RestCode(code=404, message="NOK", description="Product not found")
    })
    @RequestMapping(value="{productId}/ratings", method= RequestMethod.GET)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JRating>> getAllRatingsForProduct(HttpServletRequest request,
                                                                       Principal principal,
                                                                       @PathVariable String productId) {

        final Collection<JRating> body = rnrService.getAllRatingsForProduct(productId,
                null != principal ? principal.getName() : null);

        if (body.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Collection<JRating>>(body, HttpStatus.OK);
    }

    /**
     * Get all products a user have liked.
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a list of products
     */
    @RestReturn(value=JProductV15.class, entity=JProductV15.class, code={
            @RestCode(code=200, message="OK", description="Products found"),
    })
    @RequestMapping(value="my/liked", method= RequestMethod.POST)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JProductV15>> getProductsLikedByUser(HttpServletRequest request,
                                                                          Principal principal,
                                                                          @RequestParam(required=false) String username) {

        try {
            final Collection<JProductV15> body = rnrService.getProductsLikedByUser(username,
                    null != principal ? principal.getName() : null);

            return new ResponseEntity<Collection<JProductV15>>(body, HttpStatus.OK);
        }
        catch (IllegalArgumentException usernameNull) {
            return new ResponseEntity<Collection<JProductV15>>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get all products a user have rated.
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a list of products
     */
    @RestReturn(value=JProductV15.class, entity=JProductV15.class, code={
            @RestCode(code=200, message="OK", description="Products found"),
    })
    @RequestMapping(value="my/rated", method= RequestMethod.POST)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JProductV15>> getProductsRatedByUser(HttpServletRequest request,
                                                                          Principal principal,
                                                                          @RequestParam(required=false) String username) {
        try {
            final Collection<JProductV15> body = rnrService.getProductsRatedByUser(username,
                    null != principal ? principal.getName() : null);

            return new ResponseEntity<Collection<JProductV15>>(body, HttpStatus.OK);
        }
        catch (IllegalArgumentException usernameNull) {
            return new ResponseEntity<Collection<JProductV15>>(HttpStatus.UNAUTHORIZED);
        }
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}
