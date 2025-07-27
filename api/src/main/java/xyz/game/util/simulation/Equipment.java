package xyz.game.util.simulation;

import lombok.Data;

import java.util.List;

@Data
public class Equipment {
    private String name;
    // 神话、传说等分类
    private List<Integer> types;
    // 属性值变更 如增加攻击力、减少韧性等
    private String express;
    // 被动或者主动
    private List<Event> events;
}