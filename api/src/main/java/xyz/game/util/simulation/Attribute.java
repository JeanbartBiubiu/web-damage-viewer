package xyz.game.util.simulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import lombok.Data;

@Data
public class Attribute {
    private Value maxValue;
    private Value currentValue;
    // buff结束需要重新计算
    private int reCalculateTime = 0;

}

@Data
class Counter {

}

@Data
class Value {
    private int type;
    private double value;
}

@Data
class CurentValue extends Value {
    private Map<String, ValueSlot> valueSlots;
    private Map<String, CoefficientSlot> coefficientSlots;
    // 按时间排序的slot，用于快速判断缓存是否有效
    private TreeMap<Integer, List<String>> valueSlotsTimeIndex;
    private TreeMap<Integer, List<String>> coefficientSlotsTimeIndex;
    private double cachedValue; // 缓存的计算结果
    private int lastCalculateTime = -1; // 上次计算的时间

    /**
     * 初始化CurrentValue，设置默认的base系数
     * @param baseCoefficient 默认的base系数
     */
    public void initialize(double baseCoefficient) {
        this.valueSlots = new HashMap<>();
        this.coefficientSlots = new HashMap<>();
        this.valueSlotsTimeIndex = new TreeMap<>();
        this.coefficientSlotsTimeIndex = new TreeMap<>();

        // 添加默认的base系数slot
        CoefficientSlot baseSlot = new CoefficientSlot();
        baseSlot.setValue(baseCoefficient);
        baseSlot.setId("base");
        baseSlot.setEffectiveTime(-1); // -1表示永久有效
        this.coefficientSlots.put("base", baseSlot);

        // 更新时间索引
        updateCoefficientTimeIndex("base", -1, -1);
    }

    /**
     * 获取当前值（无时间参数版本，使用当前系统时间）
     * @return 当前计算后的值
     */
    public double getCurrentValue() {
        return getCurrentValue((int) (System.currentTimeMillis() / 1000));
    }

    /**
     * 获取指定时间的当前值
     * 计算公式：(原始值 + 所有ValueSlot的值) * 所有CoefficientSlot的系数
     * @param currentTime 当前时间
     * @return 当前计算后的值
     */
    public double getCurrentValue(int currentTime) {
        // 检查是否可以使用缓存
        if (canUseCachedValue(currentTime)) {
            return cachedValue;
        }

        // 重新计算
        double totalValue = this.getValue();

        // 加上所有有效的ValueSlot的值
         if (valueSlots != null) {
             for (ValueSlot slot : valueSlots.values()) {
                 if (slot.getEffectiveTime() == -1 || slot.getEffectiveTime() > currentTime) {
                     totalValue += slot.getValue();
                 }
             }
         }

         // 乘以所有有效的CoefficientSlot的系数
         if (coefficientSlots != null) {
             for (CoefficientSlot slot : coefficientSlots.values()) {
                 if (slot.getEffectiveTime() == -1 || slot.getEffectiveTime() > currentTime) {
                     totalValue *= slot.getValue();
                 }
             }
         }

        // 更新缓存
        cachedValue = totalValue;
        lastCalculateTime = currentTime;

        return totalValue;
    }

    /**
     * 检查是否可以使用缓存的值
     * @param currentTime 当前时间
     * @return 是否可以使用缓存
     */
    private boolean canUseCachedValue(int currentTime) {
        if (lastCalculateTime == -1) {
            return false; // 从未计算过
        }

        if (currentTime <= lastCalculateTime) {
            return true; // 时间没有前进，可以使用缓存
        }

        // 检查valueSlots中是否有在这个时间段内失效的slot
         if (valueSlotsTimeIndex != null && !valueSlotsTimeIndex.isEmpty()) {
             // 获取在时间段(lastCalculateTime, currentTime]内失效的slot
             Map<Integer, List<String>> expiredSlots = valueSlotsTimeIndex.subMap(lastCalculateTime + 1, true, currentTime, true);
             if (!expiredSlots.isEmpty()) {
                 return false; // 有slot在这个时间段内失效
             }
         }

         // 检查coefficientSlots中是否有在这个时间段内失效的slot
         if (coefficientSlotsTimeIndex != null && !coefficientSlotsTimeIndex.isEmpty()) {
             // 获取在时间段(lastCalculateTime, currentTime]内失效的slot
             Map<Integer, List<String>> expiredSlots = coefficientSlotsTimeIndex.subMap(lastCalculateTime + 1, true, currentTime, true);
             if (!expiredSlots.isEmpty()) {
                 return false; // 有slot在这个时间段内失效
             }
         }

        return true; // 可以使用缓存
    }

    /**
     * 添加或更新ValueSlot
     * @param id 标识
     * @param value 值
     * @param effectiveTime 有效时间
     */
    public void setValueSlot(String id, double value, int effectiveTime) {
        if (valueSlots == null) {
            valueSlots = new HashMap<>();
            valueSlotsTimeIndex = new TreeMap<>();
        }

        // 获取旧的effectiveTime用于更新时间索引
        int oldEffectiveTime = -1;
        ValueSlot existingSlot = valueSlots.get(id);
        if (existingSlot != null) {
            oldEffectiveTime = existingSlot.getEffectiveTime();
            existingSlot.setValue(value);
            existingSlot.setEffectiveTime(effectiveTime);
        } else {
            // 添加新slot
            ValueSlot newSlot = new ValueSlot();
            newSlot.setId(id);
            newSlot.setValue(value);
            newSlot.setEffectiveTime(effectiveTime);
            valueSlots.put(id, newSlot);
        }

        // 更新时间索引
        updateValueTimeIndex(id, effectiveTime, oldEffectiveTime);
        clearCache();
    }

    /**
     * 添加或更新CoefficientSlot
     * @param id 标识
     * @param coefficient 系数
     * @param effectiveTime 有效时间
     */
    public void setCoefficientSlot(String id, double coefficient, int effectiveTime) {
        if (coefficientSlots == null) {
            coefficientSlots = new HashMap<>();
            coefficientSlotsTimeIndex = new TreeMap<>();
        }

        // 获取旧的effectiveTime用于更新时间索引
        int oldEffectiveTime = -1;
        CoefficientSlot existingSlot = coefficientSlots.get(id);
        if (existingSlot != null) {
            oldEffectiveTime = existingSlot.getEffectiveTime();
            existingSlot.setValue(coefficient);
            existingSlot.setEffectiveTime(effectiveTime);
        } else {
            // 添加新slot
            CoefficientSlot newSlot = new CoefficientSlot();
            newSlot.setId(id);
            newSlot.setValue(coefficient);
            newSlot.setEffectiveTime(effectiveTime);
            coefficientSlots.put(id, newSlot);
        }

        // 更新时间索引
        updateCoefficientTimeIndex(id, effectiveTime, oldEffectiveTime);
        clearCache();
    }

    /**
     * 移除ValueSlot
     * @param id 标识
     * @return 是否成功移除
     */
    public boolean removeValueSlot(String id) {
        if (valueSlots == null) {
            return false;
        }
        ValueSlot removedSlot = valueSlots.remove(id);
        if (removedSlot != null) {
            // 从时间索引中移除
            removeFromValueTimeIndex(id, removedSlot.getEffectiveTime());
            clearCache();
            return true;
        }
        return false;
    }

    /**
     * 移除CoefficientSlot
     * @param id 标识
     * @return 是否成功移除
     */
    public boolean removeCoefficientSlot(String id) {
        if (coefficientSlots == null) {
            return false;
        }
        CoefficientSlot removedSlot = coefficientSlots.remove(id);
        if (removedSlot != null) {
            // 从时间索引中移除
            removeFromCoefficientTimeIndex(id, removedSlot.getEffectiveTime());
            clearCache();
            return true;
        }
        return false;
    }

    /**
     * 更新ValueSlot的时间索引
     */
    private void updateValueTimeIndex(String id, int newEffectiveTime, int oldEffectiveTime) {
        if (valueSlotsTimeIndex == null) {
            valueSlotsTimeIndex = new TreeMap<>();
        }

        // 从旧时间索引中移除
        if (oldEffectiveTime != -1) {
            removeFromValueTimeIndex(id, oldEffectiveTime);
        }

        // 添加到新时间索引（永久有效的不加入时间索引）
        if (newEffectiveTime != -1) {
            valueSlotsTimeIndex.computeIfAbsent(newEffectiveTime, k -> new ArrayList<>()).add(id);
        }
    }

    /**
     * 更新CoefficientSlot的时间索引
     */
    private void updateCoefficientTimeIndex(String id, int newEffectiveTime, int oldEffectiveTime) {
        if (coefficientSlotsTimeIndex == null) {
            coefficientSlotsTimeIndex = new TreeMap<>();
        }

        // 从旧时间索引中移除
        if (oldEffectiveTime != -1) {
            removeFromCoefficientTimeIndex(id, oldEffectiveTime);
        }

        // 添加到新时间索引（永久有效的不加入时间索引）
        if (newEffectiveTime != -1) {
            coefficientSlotsTimeIndex.computeIfAbsent(newEffectiveTime, k -> new ArrayList<>()).add(id);
        }
    }

    /**
     * 从ValueSlot时间索引中移除
     */
    private void removeFromValueTimeIndex(String id, int effectiveTime) {
        if (valueSlotsTimeIndex != null && effectiveTime != -1) {
            List<String> ids = valueSlotsTimeIndex.get(effectiveTime);
            if (ids != null) {
                ids.remove(id);
                if (ids.isEmpty()) {
                    valueSlotsTimeIndex.remove(effectiveTime);
                }
            }
        }
    }

    /**
     * 从CoefficientSlot时间索引中移除
     */
    private void removeFromCoefficientTimeIndex(String id, int effectiveTime) {
        if (coefficientSlotsTimeIndex != null && effectiveTime != -1) {
            List<String> ids = coefficientSlotsTimeIndex.get(effectiveTime);
            if (ids != null) {
                ids.remove(id);
                if (ids.isEmpty()) {
                    coefficientSlotsTimeIndex.remove(effectiveTime);
                }
            }
        }
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        lastCalculateTime = -1;
        cachedValue = 0;
    }

    /**
     * 获取指定id的ValueSlot
     * @param id 标识
     * @return ValueSlot，如果不存在则返回null
     */
    public ValueSlot getValueSlot(String id) {
        if (valueSlots == null) {
            return null;
        }
        return valueSlots.get(id);
    }

    /**
     * 获取指定id的CoefficientSlot
     * @param id 标识
     * @return CoefficientSlot，如果不存在则返回null
     */
    public CoefficientSlot getCoefficientSlot(String id) {
        if (coefficientSlots == null) {
            return null;
        }
        return coefficientSlots.get(id);
    }
}

@Data
class ValueSlot {
    private String id; // 标识
    private double value;
    private int effectiveTime;
}

@Data
class CoefficientSlot {
    private String id; // 标识
    private double value;
    private int effectiveTime;
}

