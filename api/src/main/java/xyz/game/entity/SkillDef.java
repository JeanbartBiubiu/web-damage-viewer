package xyz.game.entity;

import java.io.Serializable;

/**
 * 技能定义表(SkillDef)实体类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public class SkillDef implements Serializable {
    private static final long serialVersionUID = 609050819844317414L;

    private Integer skillId;

    private String skillName;

    private String skillImg;

    private String skillType;


    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillImg() {
        return skillImg;
    }

    public void setSkillImg(String skillImg) {
        this.skillImg = skillImg;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

}

