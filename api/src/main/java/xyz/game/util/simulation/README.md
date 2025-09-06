# 事件驱动计算引擎

## 概述

这是一个基于事件驱动的战斗计算引擎，用于模拟游戏中的战斗过程。引擎支持三种不同类型的事件，并按时间顺序执行，直到战斗结束（任意单位生命值归0）。

## 核心组件

### 1. WorldLine（世界线）
- **作用**：主要的战斗管理器，控制整个战斗流程
- **功能**：
  - 管理战斗时间流逝
  - 调度和执行事件
  - 检查战斗结束条件
  - 处理持续事件的定时检查

### 2. EventScheduler（事件调度器）
- **作用**：管理事件的调度和执行顺序
- **功能**：
  - 使用优先队列维护事件执行顺序
  - 支持事件的添加、获取和取消
  - 按时间顺序自动排序事件

### 3. TimedEvent（定时事件）
- **作用**：包含执行时间的事件包装器
- **功能**：
  - 封装原始事件和执行时间
  - 支持事件排序比较
  - 包含事件拥有者和类型信息

### 4. EventType（事件类型）
- **IMMEDIATE（立即执行事件）**：时间到了就直接执行
- **DELAYED（延迟执行事件）**：触发后有延迟时间，延迟结束后执行
- **CONTINUOUS（持续事件）**：每0.25秒检查一次，满足条件就触发

## 事件执行流程

1. **初始化阶段**：
   - 创建战斗对象（FightObject）
   - 设置属性值（生命值、魔法值、攻击力等）
   - 配置事件列表
   - 初始化世界线

2. **战斗循环**：
   - 获取下一个最近时间的事件
   - 更新当前时间到事件执行时间
   - 检查是否需要处理持续事件（每0.25秒）
   - 执行事件逻辑
   - 检查战斗是否结束

3. **事件执行**：
   - 执行前摇时间
   - 评估条件并执行动作
   - 执行后摇时间
   - 处理CD和重新调度
   - 触发订阅事件

## 使用示例

```java
// 创建战斗对象
FightObject player = createPlayer();
FightObject enemy = createEnemy();
List<FightObject> fightObjects = Arrays.asList(player, enemy);

// 创建世界线并开始战斗
WorldLine worldLine = new WorldLine(fightObjects);
worldLine.startBattle();
```

## 事件配置

### 立即执行事件示例
```java
Event normalAttack = new Event();
normalAttack.setTypes(Set.of(EventType.IMMEDIATE.getType()));
normalAttack.setQianyao(200.0); // 200ms前摇
normalAttack.setHouyao(300.0);  // 300ms后摇

// 设置CD
CD cd = new CD();
cd.setAfterCalculateV(1500.0); // 1.5秒CD
normalAttack.setCd(cd);

// 设置伤害逻辑
Doing damage = new Doing();
damage.setCondition("target.hp > 0");
damage.setChangeThing(Arrays.asList("target.hp-100", "self.mp-10"));
normalAttack.setDoings(Arrays.asList(damage));
```

### 延迟执行事件示例
```java
Event skillAttack = new Event();
skillAttack.setTypes(Set.of(EventType.DELAYED.getType()));
// 触发后会有延迟时间，然后执行
```

### 持续事件示例
```java
Event healthRegen = new Event();
healthRegen.setTypes(Set.of(EventType.CONTINUOUS.getType()));
// 每0.25秒检查一次，满足条件就执行
```

## 条件表达式

支持的条件格式：`target.attribute operator value`

- **目标**：`self`（自己）或 `target`（敌人）
- **属性**：`hp`、`mp`、`attack`、`defense` 等
- **操作符**：`>`、`>=`、`<`、`<=`、`==`、`!=`
- **示例**：
  - `self.mp >= 50`：自己的魔法值大于等于50
  - `target.hp > 0`：目标生命值大于0

## 变更指令

支持的变更格式：`target.attribute+/-value`

- **示例**：
  - `target.hp-100`：减少目标100点生命值
  - `self.mp-50`：减少自己50点魔法值
  - `self.hp+20`：增加自己20点生命值

## 扩展建议

1. **条件系统增强**：
   - 支持复合条件（AND、OR逻辑）
   - 支持更复杂的表达式计算

2. **事件系统优化**：
   - 添加事件优先级
   - 支持事件中断和取消
   - 添加事件链和组合事件

3. **属性系统完善**：
   - 支持百分比计算
   - 添加属性上限和下限
   - 支持临时属性修改

4. **性能优化**：
   - 事件池复用
   - 批量事件处理
   - 内存优化

## 注意事项

1. 所有时间单位为毫秒
2. 持续事件检查间隔固定为250毫秒（0.25秒）
3. 生命值和魔法值不会低于0
4. 事件执行顺序严格按时间排序
5. 相同时间的事件按创建顺序执行