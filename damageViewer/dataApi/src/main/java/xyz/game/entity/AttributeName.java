package xyz.game.entity;

import java.io.Serializable;

/**
 * 属性名称表(AttributeName)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public class AttributeName implements Serializable {
    private static final long serialVersionUID = -77530299099972247L;

    private Integer attributeId;

    private Integer languageId;

    private String attributeName;


    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

}

