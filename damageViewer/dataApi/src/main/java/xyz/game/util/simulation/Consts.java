package xyz.game.util.simulation;

public enum  Consts {
    COMMON_SKILL(1), // 普通技能
    SUPER_SKILL(2), // 大招
    COMMON_ATTACK(3), // 平A
    EQUIPMENT_SKILL(4), // 装备效果
    CAN_CRITICAL(5), // 可暴击判定
    INDIVIDUAL_EFFECT(6), // 单体效果
    GROUP_EFFECT(7), // 群体效果



    RESERVED(999); // 保留type


    private final int type;

    Consts(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
