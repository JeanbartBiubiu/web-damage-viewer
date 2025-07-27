package xyz.game.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 装备表(buff等状态效果也定义为装备)(Equipment)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Data
public class Equipment implements Serializable {
    private static final long serialVersionUID = -62500384248752841L;

    private Integer equipmentId;

    private String equipmentImg;
    private String equipmentName;

    // 价格
    private Integer consumption;
/**
     * 增加的属性数值表达式
     */
    private String attributeExpression;

    /**
     * 子装备，id列表
     */
    private String subEquips;

}

