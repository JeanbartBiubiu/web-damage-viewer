package xyz.game.util.simulation.event;

import lombok.Data;
import xyz.game.util.simulation.FightObject;

import java.util.List;
import java.util.Set;

@Data
public class BaseEvent {
    private Integer order;
    // 引用父原始方便找变量
    private FightObject fightObject;
    private CD cd;
    private Set<Integer> types;
    // 有顺序的;耗蓝也在doing里减掉
    private List<Doing> doings;

    // 可能还要再细分执行完进入CD还是释放技能就直接进入CD
    private Double qianyao;
    // 应该可以砍掉
    private Double houyao;

    private boolean isCriticalStrike;
    /* =============== 暴击相关 =============== */
    // 是否允许暴击；false 时直接跳过暴击逻辑
    private boolean canCrit = true;
    // 采用的暴击策略，默认 DIRECT
    private CritStrategy critStrategy = CritStrategy.DIRECT;
    // 基础暴击概率（0~1）
    private double critRate = 0.0;
    // 暴击伤害倍率（例如 1.5 表示 150%）
    private double critDamage = 1.5;
    /* ======================================== */

    /** 快速初始化暴击三要素 */
    public void initCrit(boolean canCrit, CritStrategy strategy, double critRate, double critDamage) {
      this.canCrit = canCrit;
      this.critStrategy = strategy;
      this.critRate = critRate;
      this.critDamage = critDamage;
    }

    /** 运行时动态更新暴击率与暴伤 */
    public void updateCrit(double critRate, double critDamage) {
      this.critRate = critRate;
      this.critDamage = critDamage;
    }

    // 订阅事件 触发护甲减少、攻击特效、装备效果等等；顺序：before->this->after
    private List<BaseEvent> subBeforeEvents;
    private List<BaseEvent> subAfterEvents;
}

