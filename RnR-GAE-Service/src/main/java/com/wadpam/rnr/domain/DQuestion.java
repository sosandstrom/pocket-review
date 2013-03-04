package com.wadpam.rnr.domain;

import net.sf.mardao.core.Parent;
import net.sf.mardao.core.domain.AbstractLongEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Domain object for questions.
 * @author mattiaslevin
 */
@Entity
public class DQuestion extends AbstractLongEntity {

    @Parent(kind = "DQuestion")
    private Object parent;

    /** The product id related to the question */
    @Basic
    private String productId;

    /** The original poster username */
    @Basic
    private String opUsername;

    /** The question asked */
    @Basic
    private String question;

    /** The user being asked the question */
    @Basic
    private String tagetUsername;

    /** Targeted users answer */
    @Basic
    private Long answer;

    @Override
    public String toString() {
        return String.format("{id:%d, productId:%s, question:%s}", getId(), productId, opUsername, question);
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

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }
}
