package xyz.game.entity;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 属性表(Attribute)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Data
public class Attribute implements Serializable {
    private static final long serialVersionUID = 513159944406902136L;

    private Integer attributeId;
/**
     * 属性图片
     */
    private String attributeImg;
}

