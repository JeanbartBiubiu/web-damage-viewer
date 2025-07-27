package xyz.game.entity;

import java.io.Serializable;

/**
 * (Language)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public class Language implements Serializable {
    private static final long serialVersionUID = -58736046902151921L;

    private Integer languageId;

    private String languageName;


    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

}

