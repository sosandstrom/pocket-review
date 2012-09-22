package com.wadpam.rnr.json;

import java.util.Collection;

/**
 * The Json object for a page of products.
 * @author os
 */
public class JProductPage {

    /** The cursor used to return the next page of products */
    private String cursor;

    /**
     * The number of products to return.
     * If the number of products actually returned are less then the requested page size, end of pagination have been reached.
     */
    private Long pageSize;

    /** The products */
    private Collection<JProduct> products;


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

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Collection<JProduct> getProducts() {
        return products;
    }

    public void setProducts(Collection<JProduct> products) {
        this.products = products;
    }
    
    
}
