package com.wadpam.rnr.web;


import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DAppSettings;
import com.wadpam.rnr.json.JAppSettings;
import com.wadpam.rnr.service.AppService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

/**
 * The apps controller implements all REST methods related to managing RnR applications.
 * @author mlv
 */
@Controller
@RequestMapping(value="{domain}/appSettings")
public class AppController {

    static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    private AppService appService;


    /**
     * Create app settings.
     * @param onlyLikeOnce optional. Should an identified user only be allowed to like once. Default true.
     * @param onlyRateOnce optional. Should an identified user only be allowed to rate once. Default true.
     * @return redirect to the newly create app settings
     */
    @RestReturn(value=JAppSettings.class, entity=JAppSettings.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created app settings")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public ResponseEntity<JAppSettings> createAppSettings(HttpServletRequest request,
                                                          HttpServletResponse response,
                                                          Principal principal,
                                                          @PathVariable String domain,
                                                          @RequestParam(required=false, defaultValue="true") boolean onlyLikeOnce,
                                                          @RequestParam(required=false, defaultValue="true") boolean onlyRateOnce) {

        try {
            DAppSettings body = appService.createAppSettings(domain, onlyLikeOnce, onlyRateOnce);

            response.sendRedirect(request.getRequestURI());
            return null; // No need to return anything
        }
        catch (Exception e) {
            LOG.error("Not possible to create a new app settings with reason: " + e.getMessage());
            return new ResponseEntity<JAppSettings>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get app settings.
     * @return the app settings for current domain
     */
    @RestReturn(value=JAppSettings.class, entity=JAppSettings.class, code={
            @RestCode(code=200, message="OK", description="App settings found"),
            @RestCode(code=404, message="NOK", description="No app settings found")
    })
    @RequestMapping(value="", method= RequestMethod.GET)
    public ResponseEntity<JAppSettings> getAppSettings(HttpServletRequest request,
                                                       Principal principal,
                                                       @PathVariable String domain) {

        DAppSettings body = appService.getAppSettings(domain);

        if (null == body)
            return new ResponseEntity<JAppSettings>(Converter.convert(body, request), HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JAppSettings>(Converter.convert(body, request), HttpStatus.OK);
    }


    /**
     * Delete app settings.
     * @return the and http response code indicating the outcome of the operation
     */
    @RestReturn(value=JAppSettings.class, entity=JAppSettings.class, code={
            @RestCode(code=200, message="OK", description="App settings deleted"),
            @RestCode(code=404, message="NOK", description="No app settings found")
    })
    @RequestMapping(value="", method= RequestMethod.DELETE)
    public ResponseEntity<JAppSettings> deleteAppSettings(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       Principal principal,
                                                       @PathVariable String domain) {

        DAppSettings body = appService.deleteAppSettings(domain);

        if (null == body)
            return new ResponseEntity<JAppSettings>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JAppSettings>(HttpStatus.OK);
    }



    /**
     * Generate new app key.
     * @return the app settings for current domain
     */
    @RestReturn(value=JAppSettings.class, entity=JAppSettings.class, code={
            @RestCode(code=200, message="OK", description="New app key generated"),
            @RestCode(code=404, message="NOK", description="No app settings found")
    })
    @RequestMapping(value="appKey", method= RequestMethod.POST)
    public ResponseEntity<JAppSettings> generateNewAppKey(HttpServletRequest request,
                                                          HttpServletResponse response,
                                                          Principal principal,
                                                          @PathVariable String domain) {

        DAppSettings body = appService.generateNewAppKey(domain);

        if (null == body)
            return new ResponseEntity<JAppSettings>(Converter.convert(body, request), HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<JAppSettings>(Converter.convert(body, request), HttpStatus.OK);
    }


    // Setters and Getters
    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
