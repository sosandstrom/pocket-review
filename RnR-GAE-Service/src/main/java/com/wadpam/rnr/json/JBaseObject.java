/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.json;

/**
 *
 * @author os
 */
public class JBaseObject {
    /** Unique id for this Entity in the database */
    private String id;
    
    /** Milliseconds since 1970 when this Entity was created in the database */
    private Long createdDate;
    
    /** Milliseconds since 1970 when this Entity was last updated in the database */
    private Long updatedDate;

    public JBaseObject() {
    }

    public JBaseObject(String id, Long createdDate, Long state, Long updatedDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return String.format("%s{id:%s, %s}", getClass().getSimpleName(), id, subString());
    }
    
    protected String subString() { 
        return ""; 
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

}
