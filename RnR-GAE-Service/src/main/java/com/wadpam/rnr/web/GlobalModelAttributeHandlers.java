package com.wadpam.rnr.web;

import com.wadpam.open.domain.DAppDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * This controller perform activities common to all controllers.
 * Run @ModelAttributes, @ExceptionHandler and @InitBinder
 * @author mattiaslevin
 */
@ControllerAdvice
public class GlobalModelAttributeHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalModelAttributeHandlers.class);

    // Name and attribute injected in the request
    private static final String ATTR_NAME_DOMAIN = "_domain";

    // Add the tracking code and email to the model
    @ModelAttribute()
    public void addTrackingCodeAndEmail(HttpServletRequest request, Model model) {        
        DAppDomain dAppDomain = (DAppDomain) request.getAttribute(ATTR_NAME_DOMAIN);
        if (null != dAppDomain) {

            if (null != dAppDomain.getAnalyticsTrackingCode() && !dAppDomain.getAnalyticsTrackingCode().isEmpty()) {
                model.addAttribute("trackingCode", dAppDomain.getAnalyticsTrackingCode());
            }

            if (null != dAppDomain.getEmail() && !dAppDomain.getEmail().getEmail().isEmpty()) {
                model.addAttribute("email", dAppDomain.getEmail().getEmail());
            }

        }
    }

}
