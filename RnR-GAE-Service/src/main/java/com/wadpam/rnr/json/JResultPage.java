package com.wadpam.rnr.json;

import java.util.Collection;

/**
 *
 * @author os
 */
public class JResultPage {

    private String cursor;

    private Collection<JResult> results;

    private long offset;
    
    private long limit;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public Collection<JResult> getResults() {
        return results;
    }

    public void setResults(Collection<JResult> results) {
        this.results = results;
    }
    
    
}
