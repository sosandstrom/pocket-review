package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DProduct;
import com.wadpam.rnr.json.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The product controller implements all REST methods related to products.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/product")
public class ProductController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private RnrService rnrService;


    /**
     * Get a specific product.
     * @param productId domain-unique id for the product
     * @return the product
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Product found"),
            @RestCode(code=404, message="Not Found", description="Product not found")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.GET)
    public ResponseEntity<JProduct> getProductInfo(HttpServletRequest request,
                                                   @PathVariable String productId) {

        final DProduct body = rnrService.getProduct(productId);

        return new ResponseEntity<JProduct>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    // Get the based URL of the request
    private String getBaseUrl(HttpServletRequest request) {
        // Figure out the base url
        String baseUrl = null;
        Pattern pattern = Pattern.compile("(^.*)/product");
        Matcher matcher = pattern.matcher(request.getRequestURL().toString());
        if (matcher.find())
            baseUrl = matcher.group(1);

        return baseUrl;
    }

    /**
     * Get a list of products.
     * @param ids a list of productIds
     * @return a list of products
     */
    // TODO: How to document the ids parameter?
    @RestReturn(value=JProductPage.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Products found")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="ids")
    public ResponseEntity<Collection<JProduct>> getProducts(HttpServletRequest request,
                                                            @RequestParam(value = "ids") String ids[]) {

        final Collection<DProduct> body = rnrService.getProducts(ids);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get all products.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a list of products and new cursor.
     */
    @RestReturn(value=JProductPage.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Products found")
    })
    @RequestMapping(value="", method=RequestMethod.GET)
    public ResponseEntity<JProductPage> getAllProducts(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "10") int pagesize,
                                                       @RequestParam(required = false) String cursor) {

        // Just in case
        if (null != cursor && cursor.isEmpty()) cursor = null;

        Collection<DProduct> dProducts = new ArrayList<DProduct>();
        String newCursor = rnrService.getProductPage(cursor, pagesize, dProducts);

        JProductPage body = new JProductPage();
        body.setCursor(newCursor);
        body.setPageSize((long)pagesize);
        body.setProducts(Converter.convert(dProducts, getBaseUrl(request)));

        return new ResponseEntity<JProductPage>(body, HttpStatus.OK);
    }

    /**
     * Returns a list of nearby products.
     *
     * If no latitude or longitude is provided in the request position provided by Google App Engine will be used.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @param latitude optional, the latitude to search around
     * @param longitude optional, the longitude to search around
     * @param radius optional. The radius to search with in. Default value 3000m
     * @param sort optional, the sort order of the returned results
     *             0 - distance, default value
     *             1 - average rating
     *             2 - number of likes
     * @return a list of products
     */
    @RestReturn(value=JProductPage.class, entity=JProductPage.class, code={
            @RestCode(code=200, message="OK", description="Nearby products found")
    })
    @RequestMapping(value="nearby", method=RequestMethod.GET)
    public ResponseEntity<JProductPage> findNearbyProducts(HttpServletRequest request,
                                                                   @RequestParam(defaultValue="10") int pagesize,
                                                                   @RequestParam(required=false) String cursor,
                                                                   @RequestParam(required=false) Float latitude,
                                                                   @RequestParam(required=false) Float longitude,
                                                                   @RequestParam(defaultValue="3000") int radius,
                                                                   @RequestParam(defaultValue="0") int sort)   {
        if (null == latitude) {
            final String cityLatLong = request.getHeader("X-AppEngine-CityLatLong");
            if (null != cityLatLong) {
                final int index = cityLatLong.indexOf(',');
                latitude = Float.parseFloat(cityLatLong.substring(0, index));
                longitude = Float.parseFloat(cityLatLong.substring(index + 1));
            }
        }

        final Collection<DProduct> dProducts = new ArrayList<DProduct>(pagesize);
        String newCursor = rnrService.findNearbyProducts(cursor, pagesize, latitude, longitude, radius, sort, dProducts);

        Collection<JProduct> jProducts = Converter.convert(dProducts, getBaseUrl(request));

        // Calculate the distance between the provided device position and each product in km
        for (JProduct jProduct : jProducts) {
            // Check that both the device and product position is available
            if (null != latitude && null != longitude && null != jProduct.getLocation()) {
                double distance = distFrom(latitude, longitude,
                        jProduct.getLocation().getLatitude(), jProduct.getLocation().getLongitude());
                jProduct.setDistance(new Float(distance));
            }
        }

        JProductPage body = new JProductPage();
        body.setCursor(newCursor);
        body.setPageSize((long)pagesize);
        body.setProducts(jProducts);

        return new ResponseEntity<JProductPage>(body, HttpStatus.OK);
    }

    // Calculate the distance between two points using the Harversine formula.
    // This calculation is not 100% correct but good enough and fast.
    private static double distFrom(double lat1, double long1, double lat2, double long2) {

        double earthRadius = 6371; // In km
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(long2-long1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    /**
     * Returns a list of nearby products in KML format.
     *
     * If no latitude or longitude is provided in the request position provided by Google App Engines will be used.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @param latitude optional, the latitude to search around
     * @param longitude optional, the longitude to search around
     * @param radius optional. The radius to search with in. Default value 3000m
     * @param sort optional, the sort order of the returned results
     *             0 - distance, default value
     *             1 - average rating
     *             2 - number of likes
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="Nearby products found")
    })
    @RequestMapping(value="nearby.kml", method= RequestMethod.GET)
    public void findNearbyProductsKml(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam(defaultValue="10") int pagesize,
                                      @RequestParam(required=false) String cursor,
                                      @RequestParam(required=false) Float latitude,
                                      @RequestParam(required=false) Float longitude,
                                      @RequestParam(defaultValue="3000") int radius,
                                      @RequestParam(defaultValue="0") int sort) throws IOException {
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
        rnrService.findNearbyProductsKml(cursor, pagesize, latitude, longitude, radius, sort, out);
        out.flush();
        out.close();
    }

    /**
     * Get most liked products.
     * @param limit Optional. The number of products to return. Default value 10.
     * @return a list of products sorted in most liked order
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Most liked products found")
    })
    @RequestMapping(value="liked/most", method= RequestMethod.GET)
    public ResponseEntity<Collection<JProduct>> getMostLikedProducts(HttpServletRequest request,
                                                                     @RequestParam(defaultValue = "10") int limit) {

        final Collection<DProduct> body = rnrService.getMostLikedProducts(limit);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get most thumbs up products.
     * @param limit Optional. The number of products to return. Default value 10.
     * @return a list of products sorted in most thumbs up order
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Most thumbed up products found")
    })
    @RequestMapping(value="thumbs/up/most", method= RequestMethod.GET)
    public ResponseEntity<Collection<JProduct>> getMostThumbsUpProducts(HttpServletRequest request,
                                                                        @RequestParam(defaultValue = "10") int limit) {

        final Collection<DProduct> body = rnrService.getMostThumbsUpProducts(limit);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get most thumbs down products.
     * @param limit Optional. The number of products to return. Default value 10.
     * @return a list of products sorted in most thumbs down order
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Most thumbed down products found")
    })
    @RequestMapping(value="thumbs/down/most", method= RequestMethod.GET)
    public ResponseEntity<Collection<JProduct>> getMostThumbsDownProducts(HttpServletRequest request,
                                                                          @RequestParam(defaultValue = "10") int limit) {

        final Collection<DProduct> body = rnrService.getMostThumbsDownProducts(limit);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get most rated products.
     * @param limit Optional. The number of products to return. Default value 10.
     * @return a list of products sorted in most rated order
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Most liked products found")
    })
    @RequestMapping(value="rated/most", method= RequestMethod.GET)
    public ResponseEntity<Collection<JProduct>> getMostRatedProducts(HttpServletRequest request,
                                                                     @RequestParam(defaultValue = "10") int limit) {

        final Collection<DProduct> body = rnrService.getMostRatedProducts(limit);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get top rate products.
     * @param limit Optional. The number of products to return. Default value 10.
     * @return a list of products sorted in average rating order
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Most liked products found")
    })
    @RequestMapping(value="rated/top", method= RequestMethod.GET)
    public ResponseEntity<Collection<JProduct>> getTopRatedProducts(HttpServletRequest request,
                                                                    @RequestParam(defaultValue = "10") int limit) {

        final Collection<DProduct> body = rnrService.getTopRatedProducts(limit);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get most commented products.
     * @param limit Optional. The number of products to return. Default value 10.
     * @return a list of products sorted in most commented order
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Most liked products found")
    })
    @RequestMapping(value="commented/most", method= RequestMethod.GET)
    public ResponseEntity<Collection<JProduct>> getMostcommentedProducts(HttpServletRequest request,
                                                                         @RequestParam(defaultValue = "10") int limit) {

        final Collection<DProduct> body = rnrService.getMostCommentedProducts(limit);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get all products a user have liked.
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a list of products
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Products liked by user")
    })
    @RequestMapping(value="liked", method= RequestMethod.GET)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JProduct>> getProductsLikedByUser(HttpServletRequest request,
                                                                       @RequestParam(required=true) String username) {

        final Collection<DProduct> body = rnrService.getProductsLikedByUser(username);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get all products a user have thumbed up and down.
     * @param username unique user name or id
     * @return a list of products
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Products liked by user")
    })
    @RequestMapping(value="thumbs", method= RequestMethod.GET)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JProduct>> getProductsThumbedByUser(HttpServletRequest request,
                                                                         @RequestParam(required=true) String username) {

        final Collection<DProduct> body = rnrService.getProductsThumbedByUser(username);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get all products a user have rated.
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a list of products
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Products rated by user")
    })
    @RequestMapping(value="rated", method= RequestMethod.GET)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JProduct>> getProductsRatedByUser(HttpServletRequest request,
                                                                       @RequestParam(required=true) String username) {

        final Collection<DProduct> body = rnrService.getProductsRatedByUser(username);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get all products a user have commented.
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a list of products
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Products commented by user")
    })
    @RequestMapping(value="commented", method= RequestMethod.GET)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JProduct>> getProductsCommentedByUser(HttpServletRequest request,
                                                                           @RequestParam(required=false) String username) {

        final Collection<DProduct> body = rnrService.getProductsCommentedByUser(username);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }

    /**
     * Get favorite products for a user.
     *
     * This method not only returns the product ids but all product information.
     * @param username optional.
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a list of products
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Users favorite products")
    })
    @RequestMapping(value="favorites", method= RequestMethod.GET)
    // TODO: Add pagination support
    public ResponseEntity<Collection<JProduct>> geUserFavoriteProducts(HttpServletRequest request,
                                                                       @RequestParam(required=true) String username) {

        final Collection<DProduct> body = rnrService.geUserFavoriteProducts(username);

        return new ResponseEntity<Collection<JProduct>>(Converter.convert(body, getBaseUrl(request)), HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}
