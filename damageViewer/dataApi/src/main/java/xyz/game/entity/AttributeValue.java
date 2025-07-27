package xyz.game.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 单位-属性-等级对应数据数值(AttributeValue)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Data
public class AttributeValue implements Serializable {
    private static final long serialVersionUID = 324944027450552875L;

    private Integer indivId;

    private Integer attributeId;

    private Integer levelId;

    private Double value;


}

