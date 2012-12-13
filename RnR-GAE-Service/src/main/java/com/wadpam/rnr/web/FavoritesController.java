package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.exceptions.NotFoundException;
import com.wadpam.open.web.AbstractRestController;
import com.wadpam.rnr.domain.DFavorites;
import com.wadpam.rnr.json.JFavorites;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The favorites controller implements all REST methods related to favorites.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/favorites")
public class FavoritesController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(FavoritesController.class);
    static final Converter CONVERTER = new Converter();

    private RnrService rnrService;


    /**
     * Add a product as favorite.
     * @param username unique user name or id
     * @param productId domain-unique id for the product to add as favorites
     * @return a current favorites for the user
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=302, message="OK", description="Redirect to updated users favorites")
    })
    @RequestMapping(value="{username}", method=RequestMethod.POST)
    public RedirectView addFavorite(HttpServletRequest request,
                                    HttpServletResponse response,
                                    UriComponentsBuilder uriBuilder,
                                    @PathVariable String domain,
                                    @PathVariable String username,
                                    @RequestParam(required=true) String productId) {

        final DFavorites body = rnrService.addFavorite(productId, username);

        return new RedirectView(uriBuilder.path("/{domain}/favorites/{username}").
                buildAndExpand(domain, username).toUriString());
    }

    /**
     * Remove a product from favorites.
     * @param username unique user name or id
     * @param productId domain-unique id for the product to add as favorites
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=302, message="OK", description="Redirect to updated users favorites"),
            @RestCode(code=404, message="NOK", description="Product not a favorite for the user")
    })
    @RequestMapping(value="{username}", method= RequestMethod.DELETE)
    public RedirectView deleteFavorite(HttpServletRequest request,
                                       HttpServletResponse response,
                                       UriComponentsBuilder uriBuilder,
                                       @PathVariable String domain,
                                       @PathVariable String username,
                                       @RequestParam(required=true) String productId) {


        final DFavorites body = rnrService.deleteFavorite(productId, username);
        if (null == body)
            throw new NotFoundException(404, String.format("Product:%s not found for user:%s", productId, username));

        return new RedirectView(uriBuilder.path("/{domain}/favorites/{username}").
                buildAndExpand(domain, username).toUriString());
    }

    /**
     * Get favorites for user.
     * @param username unique user name or id
     * @return users favorite products
     */
    @RestReturn(value=JFavorites.class, entity=JFavorites.class, code={
            @RestCode(code=200, message="OK", description="Favorites found for user")
    })
    @RequestMapping(value="{username}", method= RequestMethod.GET)
    public ResponseEntity<JFavorites> getFavorites(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @PathVariable String username) {

        final DFavorites body = rnrService.getFavorites(username);
        if (null == body)
            throw new NotFoundException(404, String.format("No favorites found for user:%s", username));

        return new ResponseEntity<JFavorites>(CONVERTER.convert(body), HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}
