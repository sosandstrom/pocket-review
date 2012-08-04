package com.wadpam.rnr.json;

import java.util.Collection;

/**
 *
 * @author os
 */
public class JProductPage {

    /** The cursor used to return the next page of products */
    private String cursor;

    /** The number of products to return */
    private long pageSize;

    /** The products */
    private Collection<JProductV15> products;


    @Override
    public String toString() {
        return String.format("cursor:%s page size:%d products:%s", cursor, pageSize, getProducts());
    }


    // Setters and Getters
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
