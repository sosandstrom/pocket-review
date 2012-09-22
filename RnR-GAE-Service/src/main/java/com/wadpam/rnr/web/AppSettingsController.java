package com.wadpam.rnr.web;

import com.wadpam.rnr.service.AppService;
import com.wadpam.server.web.AbstractRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The apps controller implements all REST methods related to managing application settings.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="backoffice/settings")
public class AppSettingsController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(AppSettingsController.class);

    private AppService appService;


    // Setters
    public void setAppService(AppService appService) {
        this.appService = appService;
    }
}
