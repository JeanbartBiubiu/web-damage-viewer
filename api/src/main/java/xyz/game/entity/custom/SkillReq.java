package xyz.game.entity.custom;

import lombok.Data;

@Data
public class SkillReq {
    private Integer skillId;
    private String skillName;
    private Integer skillLevel;
    private Integer skillImg;
    private Integer skillType;
    private String skillConsumption;
    private String skillCd;
    private String skillCheckAndDo;
    private String skillCastPoint;
    private String skillCastPointAfter;
}
