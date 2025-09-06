package xyz.game.util.simulation;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 战斗模拟示例
 * 展示如何使用事件驱动引擎进行战斗计算
 */
@Slf4j
public class BattleSimulationExample {

    public static void main(String[] args) {
        // 创建战斗示例
        BattleSimulationExample example = new BattleSimulationExample();
        example.runBattleSimulation();
    }

    public void runBattleSimulation() {
        log.info("开始战斗模拟示例");

        // 创建两个战斗对象
        FightObject player = createPlayer();
        FightObject enemy = createEnemy();

        List<FightObject> fightObjects = Arrays.asList(player, enemy);

        // 创建世界线并开始战斗
        WorldLine worldLine = new WorldLine(fightObjects);
        worldLine.startBattle();

        log.info("战斗模拟结束");
    }

    /**
     * 创建玩家角色
     */
    private FightObject createPlayer() {
        FightObject player = new FightObject();
        player.setName("玩家");
        player.setTypes(Arrays.asList(1)); // 玩家类型

        // 初始化属性
        player.initAttributeMapping(Arrays.asList("hp", "mp", "attack", "defense"));

        // 设置属性值
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("hp", createAttribute(1000.0)); // 1000点生命值
        attributes.put("mp", createAttribute(500.0));  // 500点魔法值
        attributes.put("attack", createAttribute(100.0)); // 100点攻击力
        attributes.put("defense", createAttribute(50.0)); // 50点防御力
        player.initAttributeMappingValue(attributes);

        // 创建技能事件
        List<Event> events = new ArrayList<>();

        // 立即执行事件 - 普通攻击
        Event normalAttack = createNormalAttackEvent();
        events.add(normalAttack);

        // 延迟执行事件 - 技能攻击
        Event skillAttack = createSkillAttackEvent();
        events.add(skillAttack);

        // 持续事件 - 生命回复
        Event healthRegen = createHealthRegenEvent();
        events.add(healthRegen);

        player.linkSkill(events);

        return player;
    }

    /**
     * 创建敌人
     */
    private FightObject createEnemy() {
        FightObject enemy = new FightObject();
        enemy.setName("敌人");
        enemy.setTypes(Arrays.asList(2)); // 敌人类型

        // 初始化属性
        enemy.initAttributeMapping(Arrays.asList("hp", "mp", "attack", "defense"));

        // 设置属性值
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("hp", createAttribute(800.0));   // 800点生命值
        attributes.put("mp", createAttribute(300.0));   // 300点魔法值
        attributes.put("attack", createAttribute(80.0)); // 80点攻击力
        attributes.put("defense", createAttribute(40.0)); // 40点防御力
        enemy.initAttributeMappingValue(attributes);

        // 创建技能事件
        List<Event> events = new ArrayList<>();

        // 立即执行事件 - 普通攻击
        Event normalAttack = createEnemyAttackEvent();
        events.add(normalAttack);

        enemy.linkSkill(events);

        return enemy;
    }

    /**
     * 创建属性对象
     */
    private Attribute createAttribute(double value) {
        Attribute attribute = new Attribute();

        Value baseValue = new Value();
        baseValue.setValue(value);

        Value totalValue = new Value();
        totalValue.setValue(value);

        Value currentValue = new Value();
        currentValue.setValue(value);

        attribute.setCurrentValue(currentValue);

        return attribute;
    }

    /**
     * 创建普通攻击事件
     */
    private Event createNormalAttackEvent() {
        Event event = new Event();
        event.setTypes(Set.of(EventType.IMMEDIATE.getType()));
        event.setQianyao(200.0); // 200ms前摇
        event.setHouyao(300.0);  // 300ms后摇

        // 设置CD
        CD cd = new CD();
        cd.setOriginV(1500.0);        // 1.5秒CD
        cd.setCurrentV(1500.0);
        cd.setAfterCalculateV(1500.0);
        event.setCd(cd);

        // 设置动作
        List<Doing> doings = new ArrayList<>();
        Doing damage = new Doing();
        damage.setCondition("target.hp > 0");
        damage.setChangeThing(Arrays.asList("target.hp-100", "self.mp-10"));
        doings.add(damage);
        event.setDoings(doings);

        return event;
    }

    /**
     * 创建技能攻击事件
     */
    private Event createSkillAttackEvent() {
        Event event = new Event();
        event.setTypes(Set.of(EventType.DELAYED.getType()));
        event.setQianyao(500.0); // 500ms前摇
        event.setHouyao(200.0);  // 200ms后摇

        // 设置CD
        CD cd = new CD();
        cd.setOriginV(3000.0);        // 3秒CD
        cd.setCurrentV(3000.0);
        cd.setAfterCalculateV(3000.0);
        event.setCd(cd);

        // 设置动作
        List<Doing> doings = new ArrayList<>();
        Doing damage = new Doing();
        damage.setCondition("self.mp >= 50");
        damage.setChangeThing(Arrays.asList("target.hp-200", "self.mp-50"));
        doings.add(damage);
        event.setDoings(doings);

        return event;
    }

    /**
     * 创建生命回复事件
     */
    private Event createHealthRegenEvent() {
        Event event = new Event();
        event.setTypes(Set.of(EventType.CONTINUOUS.getType()));

        // 设置动作
        List<Doing> doings = new ArrayList<>();
        Doing regen = new Doing();
        regen.setCondition("self.hp < self.maxHp");
        regen.setChangeThing(Arrays.asList("self.hp+5")); // 每0.25秒回复5点生命值
        doings.add(regen);
        event.setDoings(doings);

        return event;
    }

    /**
     * 创建敌人攻击事件
     */
    private Event createEnemyAttackEvent() {
        Event event = new Event();
        event.setTypes(Set.of(EventType.IMMEDIATE.getType()));
        event.setQianyao(300.0); // 300ms前摇
        event.setHouyao(400.0);  // 400ms后摇

        // 设置CD
        CD cd = new CD();
        cd.setOriginV(2000.0);        // 2秒CD
        cd.setCurrentV(2000.0);
        cd.setAfterCalculateV(2000.0);
        event.setCd(cd);

        // 设置动作
        List<Doing> doings = new ArrayList<>();
        Doing damage = new Doing();
        damage.setCondition("target.hp > 0");
        damage.setChangeThing(Arrays.asList("target.hp-80"));
        doings.add(damage);
        event.setDoings(doings);

        return event;
    }
}
