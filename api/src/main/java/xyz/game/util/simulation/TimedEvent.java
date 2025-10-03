package xyz.game.util.simulation;

import lombok.Data;
import xyz.game.util.simulation.event.BaseEvent;

/**
 * 定时事件 - 包含执行时间的事件包装器
 */
@Data
public class TimedEvent implements Comparable<TimedEvent> {

    // 事件执行时间
    private double executeTime;

    // 原始事件
    private BaseEvent event;

    // 事件拥有者
    private FightObject owner;

    // 事件类型
    private EventType eventType;

    // 事件唯一ID（用于排序时的稳定性）
    private long eventId;

    // 静态计数器，用于生成唯一ID
    private static long idCounter = 0;

    public TimedEvent(double executeTime, BaseEvent event, FightObject owner, EventType eventType) {
        this.executeTime = executeTime;
        this.event = event;
        this.owner = owner;
        this.eventType = eventType;
        this.eventId = ++idCounter;
    }

    @Override
    public int compareTo(TimedEvent other) {
        // 首先按执行时间排序
        int timeComparison = Double.compare(this.executeTime, other.executeTime);
        if (timeComparison != 0) {
            return timeComparison;
        }

        // 如果时间相同，按事件ID排序以保证稳定性
        return Long.compare(this.eventId, other.eventId);
    }

    @Override
    public String toString() {
        return String.format("TimedEvent{time=%.2f, type=%s, owner=%s, id=%d}",
                executeTime, eventType, owner.getName(), eventId);
    }
}
