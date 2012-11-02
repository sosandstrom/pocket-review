package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.json.JCursorPage;
import com.wadpam.rnr.domain.DProduct;
import com.wadpam.rnr.json.*;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;


/**
 * The product controller implements all REST methods related to products.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/product")
public class ProductController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
    static final Converter CONVERTER = new Converter();

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
                                                   HttpServletResponse response,
                                                   @PathVariable String domain,
                                                   @PathVariable String productId) {

        final DProduct body = rnrService.getProduct(productId);

        return new ResponseEntity<JProduct>(CONVERTER.convert(body, getBaseUri(request, domain)), HttpStatus.OK);
    }

    // Get the based URL of the request
    private String getBaseUri(HttpServletRequest request, String domain) {
        // Figure out the base url
        return UriComponentsBuilder.fromUriString(request.getRequestURL().toString()).
                replacePath("/api/{brand}").buildAndExpand(domain).toUriString();
    }

    /**
     * Get a list of products.
     * @param ids a list of productIds
     * @return a list of products
     */
    @RestReturn(value=JProduct.class, entity=JProduct.class, code={
            @RestCode(code=200, message="OK", description="Products found")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="ids")
    public ResponseEntity<Collection<JProduct>> getProducts(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            @PathVariable String domain,
                                                            @RequestParam(value = "ids") String ids[]) {

        final Iterable<DProduct> dProductIterable = rnrService.getProducts(ids);

        return new ResponseEntity<Collection<JProduct>>(CONVERTER.convert(dProductIterable, getBaseUri(request, domain)), HttpStatus.OK);
    }

    /**
     * Get all products.
     * @param pagesize optional. The number of products to return in this page. Default value is 10.
     * @param cursor optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a list of products and new cursor.
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Products found")
    })
    @RequestMapping(value="", method=RequestMethod.GET)
    public ResponseEntity<JCursorPage<JProduct>> getAllProducts(HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                @PathVariable String domain,
                                                                @RequestParam(defaultValue = "10") int pagesize,
                                                                @RequestParam(required = false) String cursor) {

        // Just in case
        if (null != cursor && cursor.isEmpty()) cursor = null;

        CursorPage<DProduct, String> dPage = rnrService.getProductPage(pagesize, cursor);

        JCursorPage<JProduct> body = new JCursorPage<JProduct>();
        if (null != dPage.getCursorKey())
            body.setCursor(dPage.getCursorKey().toString());
        body.setPageSize((long)pagesize);
        body.setItems(CONVERTER.convert(dPage.getItems(), getBaseUri(request, domain)));

        return new ResponseEntity<JCursorPage<JProduct>>(body, HttpStatus.OK);
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
     *             3 - number of thumbs up
     * @return a list of products
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Nearby products found")
    })
    @RequestMapping(value="nearby", method=RequestMethod.GET)
    public ResponseEntity<JCursorPage<JProduct>> findNearbyProducts(HttpServletRequest request,
                                                                    HttpServletResponse response,
                                                                    @PathVariable String domain,
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

        // Sorting order
        RnrService.SortOrder order;
        switch (sort) {
            case 1:
                order = RnrService.SortOrder.TOP_RATED;
                break;
            case 2:
                order = RnrService.SortOrder.MOST_LIKED;
                break;
            case 3:
                order = RnrService.SortOrder.MOST_THUMBS_UP;
                break;
            default:
                order = RnrService.SortOrder.DISTANCE;
                break;
        }

        CursorPage<DProduct, String> dPage = rnrService.findNearbyProducts(pagesize, cursor, latitude, longitude, radius, order);

        Collection<JProduct> jProducts = CONVERTER.convert(dPage.getItems(), getBaseUri(request, domain));

        // Calculate the distance between the provided device position and each product in km
        for (JProduct jProduct : jProducts) {
            // Check that both the device and product position is available
            if (null != latitude && null != longitude && null != jProduct.getLocation()) {
                double distance = distFrom(latitude, longitude,
                        jProduct.getLocation().getLatitude(), jProduct.getLocation().getLongitude());
                jProduct.setDistance(new Float(distance));
            }
        }

        JCursorPage<JProduct> cursorPage = new JCursorPage<JProduct>();
        if (null != dPage.getCursorKey())
            cursorPage.setCursor(dPage.getCursorKey().toString());
        cursorPage.setPageSize((long)pagesize);
        cursorPage.setItems(jProducts);

        return new ResponseEntity<JCursorPage<JProduct>>(cursorPage, HttpStatus.OK);
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
        return earthRadius * c;
    }

    /**
     * Get most liked products.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a page of products sorted in most liked order
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Most liked products found")
    })
    @RequestMapping(value="liked/most", method= RequestMethod.GET)
    public ResponseEntity<JCursorPage<JProduct>> getMostLikedProducts(HttpServletRequest request,
                                                                      HttpServletResponse response,
                                                                      @PathVariable String domain,
                                                                      @RequestParam(defaultValue="10") int pagesize,
                                                                      @RequestParam(required=false) String cursor) {

        final CursorPage<DProduct, String> dPage = rnrService.getMostLikedProducts(pagesize,  cursor);

        JCursorPage<JProduct> cursorPage = new JCursorPage<JProduct>();
        if (null != dPage.getCursorKey())
            cursorPage.setCursor(dPage.getCursorKey().toString());
        cursorPage.setPageSize((long) pagesize);
        cursorPage.setItems(CONVERTER.convert(dPage.getItems(), getBaseUri(request, domain)));

        return new ResponseEntity<JCursorPage<JProduct>>(cursorPage, HttpStatus.OK);
    }

    /**
     * Get most thumbs up products.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a page of products sorted in most thumbs up order
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Most thumbed up products found")
    })
    @RequestMapping(value="thumbs/up/most", method= RequestMethod.GET)
    public ResponseEntity<JCursorPage<JProduct>> getMostThumbsUpProducts(HttpServletRequest request,
                                                                         HttpServletResponse response,
                                                                         @PathVariable String domain,
                                                                         @RequestParam(defaultValue="10") int pagesize,
                                                                         @RequestParam(required=false) String cursor) {

        final CursorPage<DProduct, String> dPage = rnrService.getMostThumbsUpProducts(pagesize, cursor);

        JCursorPage<JProduct> cursorPage = new JCursorPage<JProduct>();
        if (null != dPage.getCursorKey())
            cursorPage.setCursor(dPage.getCursorKey().toString());
        cursorPage.setPageSize((long) pagesize);
        cursorPage.setItems(CONVERTER.convert(dPage.getItems(), getBaseUri(request, domain)));

        return new ResponseEntity<JCursorPage<JProduct>>(cursorPage, HttpStatus.OK);
    }

    /**
     * Get most thumbs down products.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a page of products sorted in most thumbs down order
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Most thumbed down products found")
    })
    @RequestMapping(value="thumbs/down/most", method= RequestMethod.GET)
    public ResponseEntity<JCursorPage<JProduct>> getMostThumbsDownProducts(HttpServletRequest request,
                                                                           HttpServletResponse response,
                                                                           @PathVariable String domain,
                                                                           @RequestParam(defaultValue="10") int pagesize,
                                                                           @RequestParam(required=false) String cursor) {

        final CursorPage<DProduct, String> dPage = rnrService.getMostThumbsDownProducts(pagesize, cursor);

        JCursorPage<JProduct> cursorPage = new JCursorPage<JProduct>();
        if (null != dPage.getCursorKey())
            cursorPage.setCursor(dPage.getCursorKey().toString());
        cursorPage.setPageSize((long) pagesize);
        cursorPage.setItems(CONVERTER.convert(dPage.getItems(), getBaseUri(request, domain)));

        return new ResponseEntity<JCursorPage<JProduct>>(cursorPage, HttpStatus.OK);
    }

    /**
     * Get most rated products.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a page of products sorted in most rated order
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Most liked products found")
    })
    @RequestMapping(value="rated/most", method= RequestMethod.GET)
    public ResponseEntity<JCursorPage<JProduct>> getMostRatedProducts(HttpServletRequest request,
                                                                      HttpServletResponse response,
                                                                      @PathVariable String domain,
                                                                      @RequestParam(defaultValue="10") int pagesize,
                                                                      @RequestParam(required=false) String cursor) {

        final CursorPage<DProduct, String> dPage = rnrService.getMostRatedProducts(pagesize, cursor);

        JCursorPage<JProduct> cursorPage = new JCursorPage<JProduct>();
        if (null != dPage.getCursorKey())
            cursorPage.setCursor(dPage.getCursorKey().toString());
        cursorPage.setPageSize((long) pagesize);
        cursorPage.setItems(CONVERTER.convert(dPage.getItems(), getBaseUri(request, domain)));

        return new ResponseEntity<JCursorPage<JProduct>>(cursorPage, HttpStatus.OK);
    }

    /**
     * Get top rate products.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a page of products sorted in average rating order
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Most liked products found")
    })
    @RequestMapping(value="rated/top", method= RequestMethod.GET)
    public ResponseEntity<JCursorPage<JProduct>> getTopRatedProducts(HttpServletRequest request,
                                                                    HttpServletResponse response,
                                                                    @PathVariable String domain,
                                                                    @RequestParam(defaultValue="10") int pagesize,
                                                                    @RequestParam(required=false) String cursor) {

        final CursorPage<DProduct, String> dPage = rnrService.getTopRatedProducts(pagesize, cursor);

        JCursorPage<JProduct> cursorPage = new JCursorPage<JProduct>();
        if (null != dPage.getCursorKey())
            cursorPage.setCursor(dPage.getCursorKey().toString());
        cursorPage.setPageSize((long) pagesize);
        cursorPage.setItems(CONVERTER.convert(dPage.getItems(), getBaseUri(request, domain)));

        return new ResponseEntity<JCursorPage<JProduct>>(cursorPage, HttpStatus.OK);
    }

    /**
     * Get most commented products.
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a page of products sorted in most commented order
     */
    @RestReturn(value=JCursorPage.class, entity=JCursorPage.class, code={
            @RestCode(code=200, message="OK", description="Most liked products found")
    })
    @RequestMapping(value="commented/most", method= RequestMethod.GET)
    public ResponseEntity<JCursorPage<JProduct>> getMostcommentedProducts(HttpServletRequest request,
                                                                         HttpServletResponse response,
                                                                         @PathVariable String domain,
                                                                         @RequestParam(defaultValue="10") int pagesize,
                                                                         @RequestParam(required=false) String cursor) {

        final CursorPage<DProduct, String> dPage = rnrService.getMostCommentedProducts(pagesize, cursor);

        JCursorPage<JProduct> cursorPage = new JCursorPage<JProduct>();
        if (null != dPage.getCursorKey())
            cursorPage.setCursor(dPage.getCursorKey().toString());
        cursorPage.setPageSize((long) pagesize);
        cursorPage.setItems(CONVERTER.convert(dPage.getItems(), getBaseUri(request, domain)));

        return new ResponseEntity<JCursorPage<JProduct>>(cursorPage, HttpStatus.OK);
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
    public ResponseEntity<Collection<JProduct>> getProductsLikedByUser(HttpServletRequest request,
                                                                       HttpServletResponse response,
                                                                       @PathVariable String domain,
                                                                       @RequestParam(required=true) String username) {

        final Iterable<DProduct> dProductIterable = rnrService.getProductsLikedByUser(username);

        return new ResponseEntity<Collection<JProduct>>(CONVERTER.convert(dProductIterable, getBaseUri(request, domain)), HttpStatus.OK);
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
    public ResponseEntity<Collection<JProduct>> getProductsThumbedByUser(HttpServletRequest request,
                                                                         HttpServletResponse response,
                                                                         @PathVariable String domain,
                                                                         @RequestParam(required=true) String username) {

        final Iterable<DProduct> dProductIterable = rnrService.getProductsThumbedByUser(username);

        return new ResponseEntity<Collection<JProduct>>(CONVERTER.convert(dProductIterable, getBaseUri(request, domain)), HttpStatus.OK);
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
    public ResponseEntity<Collection<JProduct>> getProductsRatedByUser(HttpServletRequest request,
                                                                       @PathVariable String domain,
                                                                       @RequestParam(required=true) String username) {

        final Iterable<DProduct> dProductIterable = rnrService.getProductsRatedByUser(username);

        return new ResponseEntity<Collection<JProduct>>(CONVERTER.convert(dProductIterable, getBaseUri(request, domain)), HttpStatus.OK);
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
    public ResponseEntity<Collection<JProduct>> getProductsCommentedByUser(HttpServletRequest request,
                                                                           @PathVariable String domain,
                                                                           @RequestParam(required=false) String username) {

        final Iterable<DProduct> dProductIterable = rnrService.getProductsCommentedByUser(username);

        return new ResponseEntity<Collection<JProduct>>(CONVERTER.convert(dProductIterable, getBaseUri(request, domain)), HttpStatus.OK);
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
    public ResponseEntity<Collection<JProduct>> geUserFavoriteProducts(HttpServletRequest request,
                                                                       @PathVariable String domain,
                                                                       @RequestParam(required=true) String username) {

        final Iterable<DProduct> dProductIterable = rnrService.geUserFavoriteProducts(username);

        return new ResponseEntity<Collection<JProduct>>(CONVERTER.convert(dProductIterable, getBaseUri(request, domain)), HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}
