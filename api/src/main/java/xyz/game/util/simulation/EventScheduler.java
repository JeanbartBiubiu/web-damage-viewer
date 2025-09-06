package xyz.game.util.simulation;

import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;

/**
 * 事件调度器 - 管理事件的调度和执行顺序
 */
@Slf4j
public class EventScheduler {
    
    // 使用优先队列来维护事件的执行顺序
    private PriorityQueue<TimedEvent> eventQueue;
    
    public EventScheduler() {
        this.eventQueue = new PriorityQueue<>();
    }
    
    /**
     * 调度一个事件
     * @param timedEvent 定时事件
     */
    public void scheduleEvent(TimedEvent timedEvent) {
        eventQueue.offer(timedEvent);
        log.debug("调度事件: {} 执行时间: {}", 
                timedEvent.getEventType(), timedEvent.getExecuteTime());
    }
    
    /**
     * 获取下一个要执行的事件
     * @return 下一个事件，如果队列为空则返回null
     */
    public TimedEvent getNextEvent() {
        TimedEvent nextEvent = eventQueue.poll();
        if (nextEvent != null) {
            log.debug("获取下一个事件: {}", nextEvent);
        }
        return nextEvent;
    }
    
    /**
     * 查看下一个事件但不移除
     * @return 下一个事件，如果队列为空则返回null
     */
    public TimedEvent peekNextEvent() {
        return eventQueue.peek();
    }
    
    /**
     * 检查事件队列是否为空
     * @return 如果队列为空返回true
     */
    public boolean isEmpty() {
        return eventQueue.isEmpty();
    }
    
    /**
     * 获取队列中事件的数量
     * @return 事件数量
     */
    public int size() {
        return eventQueue.size();
    }
    
    /**
     * 清空所有事件
     */
    public void clear() {
        eventQueue.clear();
        log.debug("清空事件调度器");
    }
    
    /**
     * 取消特定类型的事件
     * @param owner 事件拥有者
     * @param eventType 事件类型
     */
    public void cancelEvents(FightObject owner, EventType eventType) {
        eventQueue.removeIf(timedEvent -> 
            timedEvent.getOwner().equals(owner) && 
            timedEvent.getEventType().equals(eventType));
        log.debug("取消事件: 拥有者={}, 类型={}", owner.getName(), eventType);
    }
    
    /**
     * 取消特定拥有者的所有事件
     * @param owner 事件拥有者
     */
    public void cancelAllEvents(FightObject owner) {
        eventQueue.removeIf(timedEvent -> timedEvent.getOwner().equals(owner));
        log.debug("取消所有事件: 拥有者={}", owner.getName());
    }
}