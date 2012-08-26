package com.wadpam.rnr.json;

import java.util.Map;

/**
 * Represent a histogram of all ratings for a product
 */
public class JHistogram {

    /** The interval used in the returned histogram */
    private int interval;

    /** The histogram */
    private Map<Long, Long> histogram;


    // Setters and getters
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Map<Long, Long> getHistogram() {
        return histogram;
    }

    public void setHistogram(Map<Long, Long> histogram) {
        this.histogram = histogram;
    }
}
