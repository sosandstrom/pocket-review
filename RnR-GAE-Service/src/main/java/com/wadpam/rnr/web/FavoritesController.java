package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DFavorites;
import com.wadpam.rnr.json.JFavorites;
import com.wadpam.rnr.service.RnrService;
import com.wadpam.server.web.AbstractRestController;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

/**
 * The favorites controller implements all REST methods related to favorites.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/favorites")
public class FavoritesController extends AbstractRestController {

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
                                                  HttpServletResponse response,
                                                  Principal principal,
                                                  @PathVariable String username,
                                                  @RequestParam(required=true) String productId) {

        final DFavorites body = rnrService.addFavorite(productId, username);

        try {
            response.sendRedirect(request.getRequestURI());
            return null; // No need to return anything, the redirect takes care of it
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect to url after creating a new favorite with reason:{}", e.getMessage());
            return new ResponseEntity<JFavorites>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
                                                     HttpServletResponse response,
                                                     Principal principal,
                                                     @PathVariable String username,
                                                     @RequestParam(required=true) String productId) {


        final DFavorites body = rnrService.deleteFavorite(productId, username);

        // Derive the redirect url
        String redirectUrl = request.getRequestURI();
        int i = redirectUrl.indexOf("?");
        if (i > -1)
            redirectUrl = redirectUrl.substring(0, i - 1);

        // Do the redirect
        try {
            response.sendRedirect(request.getRequestURI());
            return null; // No need to return anything, the redirect takes care of it
        }
        catch (IOException e) {
            LOG.error("Not possible to create redirect to url after deleting a favorite with reason:{}", e.getMessage());
            return new ResponseEntity<JFavorites>(HttpStatus.INTERNAL_SERVER_ERROR);
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

        final DFavorites body = rnrService.getFavorites(username);

        return new ResponseEntity<JFavorites>(Converter.convert(body), HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
