package xyz.game.util.simulation.event;

import java.util.concurrent.ThreadLocalRandom;

public final class CritModule {

    private CritModule() {}

    /* 累计概率期望的静态变量，如果多线程请用 ThreadLocal 或 Atomic */
    private static double accumulated = 0.0;

    /**
     * 入口方法：根据 event 的配置计算最终伤害
     */
    public static double apply(double rawDamage, BaseEvent event) {
        // 1. 能否暴击
        if (!event.isCanCrit()) {
            event.setCriticalStrike(false);
            return rawDamage;
        }

        // 2. 选策略
        CritStrategy strategy = event.getCritStrategy();
        double critRate   = event.getCritRate();
        double critDamage = event.getCritDamage();

        boolean crit = false;
        switch (strategy) {
            case DIRECT:
                // 直接乘期望
                return rawDamage * (1 + critRate * (critDamage - 1));

            case ACCUMULATE:
                // 累计期望
                accumulated += critRate;
                if (accumulated >= 1.0) {
                    crit = true;
                    accumulated -= 1.0;
                }
                break;

            case RANDOM:
                // 真随机
                crit = ThreadLocalRandom.current().nextDouble() < critRate;
                break;
        }

        // 3. 应用暴击
        event.setCriticalStrike(crit);
        return crit ? rawDamage * critDamage : rawDamage;
    }
}
