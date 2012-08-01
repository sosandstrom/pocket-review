package com.wadpam.rnr.json;

import java.util.Collection;

/**
 *
 * @author os
 */
public class JProductPage {

    private String cursor;

    private long pageSize;

    private Collection<JProductV15> products;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public Collection<JProductV15> getProducts() {
        return products;
    }

    public void setProducts(Collection<JProductV15> products) {
        this.products = products;
    }
    
    
}
