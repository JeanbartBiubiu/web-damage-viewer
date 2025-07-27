package xyz.game.entity;

import java.io.Serializable;

/**
 * (Individual)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public class Individual implements Serializable {
    private static final long serialVersionUID = 772306977692146963L;
/**
     * 英雄单位标识
     */
    private Integer indivId;
/**
     * 头像
     */
    private String indivImg;


    public Integer getIndivId() {
        return indivId;
    }

    public void setIndivId(Integer indivId) {
        this.indivId = indivId;
    }

    public String getIndivImg() {
        return indivImg;
    }

    public void setIndivImg(String indivImg) {
        this.indivImg = indivImg;
    }

}

