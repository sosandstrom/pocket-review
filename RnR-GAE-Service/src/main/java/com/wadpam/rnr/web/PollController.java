package com.wadpam.rnr.web;

import com.wadpam.open.web.AbstractRestController;

/**
 * Controller for managing polls.
 *
 * A poll can be binary - yes or no, black or white or similar.
 * It can be three way question - yes, neutral or no.
 * It can be scaled - 1 to 5, good/very good/excellent.
 *
 * It is up to the application context to define the polling options.
 * Options needs to be represented as number (positive or negative)
 * towards the polling service.
 * E.g. no, neutral, yes can be represented as -1, 0, 1
 *
 * @author mattiaslevin
 */
public class PollController extends AbstractRestController {

}
