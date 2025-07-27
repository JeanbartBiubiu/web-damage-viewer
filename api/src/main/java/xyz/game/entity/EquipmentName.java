package xyz.game.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (EquipmentName)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Data
public class EquipmentName implements Serializable {
    private static final long serialVersionUID = -78473568208455997L;

    private Integer equipmentId;

    private Integer languageId;

    private String equipmentName;

}

