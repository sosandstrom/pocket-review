package com.wadpam.rnr.json;

/**
 * Created with IntelliJ IDEA.
 * User: mattias
 * Date: 7/24/12
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class JLike extends JBaseObject {

    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** The location of the product */
    private JLocation location;


    // Constructors
     public JLike() {
         // Do nothing
    }

    public JLike(String id, Long createdDate, Long updatedDate) {
        super(id, createdDate, updatedDate, updatedDate);
    }


    @Override
    protected String subString() {
        return String.format("productId:%s, username:%s, location:%s",
                productId, username, location);
    }


    // Setters and getters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;

    }

    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }
}
