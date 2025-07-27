package xyz.game.entity.custom;

import lombok.Data;
import xyz.game.entity.EquipId;

import java.util.List;

@Data
public class EquipmentReq {
    private Integer equipmentId;
    private Integer languageId;
    private String equipmentName;

    private String equipmentImg;

    private Integer consumption;
    private String attributeExpression;

    private List<EquipId> equipIds;

}
