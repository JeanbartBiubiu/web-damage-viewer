package xyz.game.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (SkillSub)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Data
public class SkillSub implements Serializable {
    private static final long serialVersionUID = -77742090076131519L;

    private Integer skillId;

    private Integer skillLevel;

    private Integer cycleId;

    private String skillConsumption;

    private String skillCd;

    private String skillCheckAndDo;

    private String skillCastPoint;

    private String skillCastPointAfter;

}

