package xyz.game.util.simulation.event;

public enum CritStrategy {
    // 1. 直接乘期望：damage * critRate * critDamage
    DIRECT,
    // 2. 累计概率期望：每次把 critRate 累加，≥1 时暴击并重置
    ACCUMULATE,
    // 3. 真随机：Random 判定一次
    RANDOM
}
