package com.wadpam.rnr.json;

import com.wadpam.open.json.JBaseObject;

/**
 * Json object for questions.
 * @author mattiaslevin
 */
public class JQuestion extends JBaseObject {

    private Object parent;

    /** The product id related to the question */
    private String productId;

    /** The original poster username */
    private String opUsername;

    /** The question asked */
    private String question;

    /** The user being asked the question */
    private String tagetUsername;

    /** Targeted users answer */
    private Long answer;

    @Override
    protected String subString() {
        return String.format("productId:%s, username:%s, question:%s answer:%d",
                productId, opUsername, question, answer);
    }

    // Setters and getters
    public Long getAnswer() {
        return answer;
    }

    public void setAnswer(Long answer) {
        this.answer = answer;
    }

    public String getOpUsername() {
        return opUsername;
    }

    public void setOpUsername(String opUsername) {
        this.opUsername = opUsername;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTagetUsername() {
        return tagetUsername;
    }

    public void setTagetUsername(String tagetUsername) {
        this.tagetUsername = tagetUsername;
    }
}
