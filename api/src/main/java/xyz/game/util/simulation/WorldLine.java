package xyz.game.util.simulation;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 世界线 - 事件驱动的计算引擎
 * 管理战斗中的所有事件执行和时间流逝
 */
@Data
@Slf4j
public class WorldLine {

    // 当前世界时间（毫秒）
    private double currentTime = 0.0;

    // 事件调度器
    private EventScheduler eventScheduler;

    // 参与战斗的对象列表
    private List<FightObject> fightObjects;

    // 战斗是否结束
    private boolean battleEnded = false;

    // 持续事件检查间隔（0.25秒 = 250毫秒）
    private static final double CONTINUOUS_EVENT_INTERVAL = 250.0;

    // 下次持续事件检查时间
    private double nextContinuousCheckTime = CONTINUOUS_EVENT_INTERVAL;

    public WorldLine(List<FightObject> fightObjects) {
        this.fightObjects = fightObjects;
        this.eventScheduler = new EventScheduler();
        initializeEvents();
    }

    /**
     * 初始化所有事件
     */
    private void initializeEvents() {
        for (FightObject fightObject : fightObjects) {
            // 初始化每个战斗对象的事件
            for (Event event : fightObject.getEvents()) {
                // 根据事件类型进行不同的初始化
                initializeEventByType(event, fightObject);
            }
        }
    }

    /**
     * 根据事件类型初始化事件
     */
    private void initializeEventByType(Event event, FightObject owner) {
        Set<Integer> types = event.getTypes();

        if (types.contains(EventType.IMMEDIATE.getType())) {
            // 立即执行事件，直接加入调度器
            eventScheduler.scheduleEvent(new TimedEvent(currentTime, event, owner, EventType.IMMEDIATE));
        } else if (types.contains(EventType.DELAYED.getType())) {
            // 延迟执行事件，需要触发条件
            // 这里可以根据具体逻辑添加触发条件检查
        } else if (types.contains(EventType.CONTINUOUS.getType())) {
            // 持续事件，在持续事件检查时处理
        }
    }

    /**
     * 开始战斗循环
     */
    public void startBattle() {
        log.info("战斗开始，当前时间: {}", currentTime);

        while (!battleEnded && !eventScheduler.isEmpty()) {
            // 获取下一个最近时间的事件
            TimedEvent nextEvent = eventScheduler.getNextEvent();

            if (nextEvent == null) {
                break;
            }

            // 更新当前时间到事件执行时间
            currentTime = nextEvent.getExecuteTime();

            // 检查是否需要处理持续事件
            if (currentTime >= nextContinuousCheckTime) {
                processContinuousEvents();
                nextContinuousCheckTime += CONTINUOUS_EVENT_INTERVAL;
            }

            // 执行事件
            executeEvent(nextEvent);

            // 检查战斗是否结束（任意单位生命值归0）
            checkBattleEnd();
        }

        log.info("战斗结束，最终时间: {}", currentTime);
    }

    /**
     * 执行事件
     */
    private void executeEvent(TimedEvent timedEvent) {
        Event event = timedEvent.getEvent();
        FightObject owner = timedEvent.getOwner();

        log.debug("执行事件: {} 在时间: {} 由 {} 触发",
                event.getClass().getSimpleName(), currentTime, owner.getName());

        // 执行前摇
        if (event.getQianyao() != null && event.getQianyao() > 0) {
            // 前摇时间处理
            currentTime += event.getQianyao();
        }

        // 执行事件的具体逻辑
        if (event.getDoings() != null) {
            for (Doing doing : event.getDoings()) {
                executeDoing(doing, owner);
            }
        }

        // 执行后摇
        if (event.getHouyao() != null && event.getHouyao() > 0) {
            currentTime += event.getHouyao();
        }

        // 处理CD
        if (event.getCd() != null) {
            double cdTime = event.getCd().getAfterCalculateV();
            if (cdTime > 0) {
                // 重新调度这个事件
                eventScheduler.scheduleEvent(new TimedEvent(
                    currentTime + cdTime, event, owner, timedEvent.getEventType()));
            }
        }

        // 处理订阅事件
        if (event.getSubEvents() != null) {
            for (Event subEvent : event.getSubEvents()) {
                triggerDelayedEvent(subEvent, owner);
            }
        }
    }

    /**
     * 执行具体的动作
     */
    private void executeDoing(Doing doing, FightObject owner) {
        // 检查条件
        if (doing.getCondition() != null && !evaluateCondition(doing.getCondition(), owner)) {
            return;
        }

        // 执行变更
        if (doing.getChangeThing() != null) {
            for (String change : doing.getChangeThing()) {
                executeChange(change, owner);
            }
        }
    }

    /**
     * 评估条件
     */
    private boolean evaluateCondition(String condition, FightObject owner) {
        // 解析条件表达式，格式：target.attribute operator value
        try {
            condition = condition.trim();

            // 支持的操作符
            String[] operators = {">=", "<=", ">", "<", "==", "!="};
            String operator = null;
            String[] parts = null;

            // 找到操作符
            for (String op : operators) {
                if (condition.contains(op)) {
                    operator = op;
                    parts = condition.split(op);
                    break;
                }
            }

            if (operator == null || parts == null || parts.length != 2) {
                log.warn("无效的条件表达式: {}", condition);
                return false;
            }

            String leftSide = parts[0].trim();
            String rightSide = parts[1].trim();

            // 解析左侧（属性引用）
            double leftValue = parseAttributeReference(leftSide, owner);

            // 解析右侧（数值或属性引用）
            double rightValue;
            if (rightSide.contains(".")) {
                // 属性引用
                rightValue = parseAttributeReference(rightSide, owner);
            } else {
                // 直接数值
                rightValue = Double.parseDouble(rightSide);
            }

            // 执行比较
            boolean result;
            switch (operator) {
                case ">":
                    result = leftValue > rightValue;
                    break;
                case ">=":
                    result = leftValue >= rightValue;
                    break;
                case "<":
                    result = leftValue < rightValue;
                    break;
                case "<=":
                    result = leftValue <= rightValue;
                    break;
                case "==":
                    result = Math.abs(leftValue - rightValue) < 0.001; // 浮点数比较
                    break;
                case "!=":
                    result = Math.abs(leftValue - rightValue) >= 0.001;
                    break;
                default:
                    log.warn("未知的操作符: {}", operator);
                    return false;
            }

            log.debug("条件评估: {} ({} {} {}) = {}",
                condition, leftValue, operator, rightValue, result);
            return result;

        } catch (Exception e) {
            log.error("评估条件时发生错误: {} - {}", condition, e.getMessage());
            return false;
        }
    }

    /**
     * 解析属性引用
     */
    private double parseAttributeReference(String reference, FightObject owner) {
        String[] parts = reference.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("无效的属性引用: " + reference);
        }

        String target = parts[0];
        String attributeName = parts[1];

        FightObject targetObject;
        if ("self".equals(target)) {
            targetObject = owner;
        } else if ("target".equals(target)) {
            // 找到第一个不是owner的对象作为目标
            targetObject = fightObjects.stream()
                .filter(obj -> !obj.equals(owner))
                .findFirst()
                .orElse(null);
            if (targetObject == null) {
                throw new IllegalArgumentException("找不到目标对象");
            }
        } else {
            throw new IllegalArgumentException("未知的目标类型: " + target);
        }

        Attribute attribute = targetObject.getAttributeMapping().get(attributeName);
        if (attribute == null) {
            throw new IllegalArgumentException(
                String.format("对象 %s 没有属性: %s", targetObject.getName(), attributeName));
        }

        return attribute.getCurrentValue().getValue();
    }

    /**
     * 执行变更
     */
    private void executeChange(String change, FightObject owner) {
        // 解析变更指令，格式：target.attribute+/-value 或 self.attribute+/-value
        try {
            String[] parts = change.split("\\.");
            if (parts.length != 2) {
                log.warn("无效的变更指令格式: {}", change);
                return;
            }

            String target = parts[0]; // self 或 target
            String attributeChange = parts[1]; // attribute+/-value

            // 解析属性和数值变更
            String operator;
            String attributeName;
            double value;

            if (attributeChange.contains("+")) {
                operator = "+";
                String[] attrParts = attributeChange.split("\\+");
                attributeName = attrParts[0];
                value = Double.parseDouble(attrParts[1]);
            } else if (attributeChange.contains("-")) {
                operator = "-";
                String[] attrParts = attributeChange.split("-");
                attributeName = attrParts[0];
                value = Double.parseDouble(attrParts[1]);
            } else {
                log.warn("无效的变更操作符: {}", attributeChange);
                return;
            }

            // 确定目标对象
            FightObject targetObject;
            if ("self".equals(target)) {
                targetObject = owner;
            } else if ("target".equals(target)) {
                // 简化处理：找到第一个不是owner的对象作为目标
                targetObject = fightObjects.stream()
                    .filter(obj -> !obj.equals(owner))
                    .findFirst()
                    .orElse(null);
                if (targetObject == null) {
                    log.warn("找不到目标对象");
                    return;
                }
            } else {
                log.warn("未知的目标类型: {}", target);
                return;
            }

            // 执行属性变更
            Attribute attribute = targetObject.getAttributeMapping().get(attributeName);
            if (attribute == null) {
                log.warn("目标对象 {} 没有属性: {}", targetObject.getName(), attributeName);
                return;
            }

            double currentValue = attribute.getCurrentValue().getValue();
            double newValue;

            if ("+".equals(operator)) {
                newValue = currentValue + value;
                log.debug("增加属性: {} 的 {} 从 {} 到 {}",
                    targetObject.getName(), attributeName, currentValue, newValue);
            } else {
                newValue = currentValue - value;
                log.debug("减少属性: {} 的 {} 从 {} 到 {}",
                    targetObject.getName(), attributeName, currentValue, newValue);
            }

            // 确保属性值不会小于0（除非是特殊属性）
            if ("hp".equals(attributeName) || "mp".equals(attributeName)) {
                newValue = Math.max(0, newValue);
            }

            attribute.getCurrentValue().setValue(newValue);

        } catch (Exception e) {
            log.error("执行变更时发生错误: {} - {}", change, e.getMessage());
        }
    }

    /**
     * 触发延迟事件
     */
    private void triggerDelayedEvent(Event event, FightObject owner) {
        if (event.getTypes().contains(EventType.DELAYED.getType())) {
            // 计算延迟时间（这里可以根据具体逻辑计算）
            double delayTime = calculateDelayTime(event);
            eventScheduler.scheduleEvent(new TimedEvent(
                currentTime + delayTime, event, owner, EventType.DELAYED));
        }
    }

    /**
     * 计算延迟时间
     */
    private double calculateDelayTime(Event event) {
        // 这里可以根据事件的具体属性计算延迟时间
        // 暂时返回固定值
        return 1000.0; // 1秒延迟
    }

    /**
     * 处理持续事件
     */
    private void processContinuousEvents() {
        for (FightObject fightObject : fightObjects) {
            for (Event event : fightObject.getEvents()) {
                if (event.getTypes().contains(EventType.CONTINUOUS.getType())) {
                    // 检查持续事件的触发条件
                    if (shouldTriggerContinuousEvent(event, fightObject)) {
                        executeEvent(new TimedEvent(currentTime, event, fightObject, EventType.CONTINUOUS));
                    }
                }
            }
        }
    }

    /**
     * 判断是否应该触发持续事件
     */
    private boolean shouldTriggerContinuousEvent(Event event, FightObject owner) {
        // 这里可以实现具体的持续事件触发条件
        return true;
    }

    /**
     * 检查战斗是否结束
     */
    private void checkBattleEnd() {
        for (FightObject fightObject : fightObjects) {
            Attribute hpAttribute = fightObject.getAttributeMapping().get("hp");
            if (hpAttribute != null && hpAttribute.getCurrentValue().getValue() <= 0) {
                battleEnded = true;
                log.info("战斗结束: {} 的生命值归零", fightObject.getName());
                break;
            }
        }
    }
}
