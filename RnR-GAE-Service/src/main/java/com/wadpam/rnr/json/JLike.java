package com.wadpam.rnr.json;

/**
 * Json object for likes.
 * @author mlv
 */
public class JLike extends JBaseObject {

    /**  The unique id of the like */
    private String             id;

    /** The Many-To-One productId (unconstrained) */
    private String             productId;

    /** The Many-To-One username (unconstrained) */
    private String             username;

    /** The location of the product */
    private JLocation          location;


    // Constructors
     public JLike() {
         // Do nothing
    }

    public JLike(String id, Long createdDate, Long updatedDate) {
        super(id, createdDate, updatedDate, updatedDate);
    }


    @Override
    protected String subString() {
        return String.format("id:%s productId:%s, username:%s, location:%s",
                id, productId, username, location);
    }


    // Setters and getters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
