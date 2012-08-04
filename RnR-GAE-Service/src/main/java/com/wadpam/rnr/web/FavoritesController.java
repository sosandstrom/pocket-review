package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DFavorites;
import com.wadpam.rnr.domain.DLike;
import com.wadpam.rnr.json.JFavorites;
import com.wadpam.rnr.json.JLike;
import com.wadpam.rnr.json.JProductV15;
import com.wadpam.rnr.service.RnrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/24/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value="{domain}/favorites")
public class FavoritesController {

    static final Logger LOG = LoggerFactory.getLogger(FavoritesController.class);

    private RnrService rnrService;

    /**
     * Add a product as favorite.
     * @param username optional. The user name.
     * @param productId domain-unique id for the product to add as favorites
     * If authenticated, and RnrService.fallbackPrincipalName,
     * principal.name will be used if username is null.
     * @return a current favorites for the user
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=302, message="OK", description="Redirect to updated users favorites")
    })
    @RequestMapping(value="{username}", method=RequestMethod.POST)
    public ResponseEntity<JFavorites> addFavorite(HttpServletRequest request,
                                             Principal principal,
                                             @PathVariable String username,
                                             @RequestParam(required=true) String productId) {


        try {
            final DFavorites body = rnrService.addFavorite(productId, username,
                    null != principal ? principal.getName() : null);
        }
        catch (IllegalArgumentException usernameNull) {
            return new ResponseEntity<JFavorites>(HttpStatus.UNAUTHORIZED);
        }

        return redirectResponseEntity(request.getRequestURI());
    }

    /**
     * Remove a product from favorites.
     * @param username optional. The user name.
     * @param productId domain-unique id for the product to add as favorites
     * If authenticated, and RnrService.fallbackPrincipalName,
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=302, message="OK", description="Redirect to updated users favorites"),
            @RestCode(code=404, message="NOK", description="Product not a favorite for the user")
    })
    @RequestMapping(value="{username}", method= RequestMethod.DELETE)
    public ResponseEntity<JFavorites> deleteFavorite(HttpServletRequest request,
                                                      Principal principal,
                                                      @PathVariable String username,
                                                      @RequestParam(required=true) String productId) {

        try {
            final DFavorites body = rnrService.deleteFavorite(productId, username,
                    null != principal ? principal.getName() : null);

            if (null == body)
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            else {

                String redirectUrl = request.getRequestURI();
                int i = redirectUrl.indexOf("?");
                if (i > -1)
                    redirectUrl = redirectUrl.substring(0, i-1);

                return redirectResponseEntity(redirectUrl);
            }
        }
        catch (IllegalArgumentException usernameNull) {
            return new ResponseEntity<JFavorites>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get favorites for user.
     * @param username optional. The user name.
     * @return users favorite products
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=200, message="OK", description="Favorites found for user")
    })
    @RequestMapping(value="{username}", method= RequestMethod.GET)
    public ResponseEntity<JFavorites> getFavorites(HttpServletRequest request,
                                            Principal principal,
                                            @PathVariable String username) {

        try {
            final DFavorites body = rnrService.getFavorites(username, null != principal ? principal.getName() : null);;

            return new ResponseEntity<JFavorites>(Converter.convert(body, request), HttpStatus.OK);
        }
        catch (IllegalArgumentException usernameNull) {
            return new ResponseEntity<JFavorites>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Create a Response entity that redirect to an URL
    private static <T> ResponseEntity<T> redirectResponseEntity(String redirectURL) {

        // Manually build the redirect
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setLocation(new URI(redirectURL));

        } catch (URISyntaxException e) {
            LOG.error("Not possible to create redirect url from string " + redirectURL);
            return new ResponseEntity<T>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<T>(headers, HttpStatus.MOVED_TEMPORARILY);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
