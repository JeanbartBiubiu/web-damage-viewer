package xyz.game.util.simulation;

/**
 * 事件类型枚举
 */
public enum EventType {
    IMMEDIATE(1),    // 立即执行事件
    DELAYED(2),      // 延迟执行事件
    CONTINUOUS(3);   // 持续事件
    
    private final int type;
    
    EventType(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }
}