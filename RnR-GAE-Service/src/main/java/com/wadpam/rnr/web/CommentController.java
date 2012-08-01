package com.wadpam.rnr.web;

import com.wadpam.rnr.service.RnrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/27/12
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value="{domain}/comment")
public class CommentController {

    static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    private RnrService rnrService;


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
