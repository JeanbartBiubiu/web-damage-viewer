package xyz.game.util.simulation;


import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FightObject {
    private String name;
    private List<Integer> types;
    private Map<String, Attribute> attributeMapping = new HashMap<>();

    // 包括装备的技能
    private List<Event> events = new ArrayList<>();
    private List<Equipment> equipments = new ArrayList<>();

    public void decAttributeValue(String attributeName, Double value){
        Attribute attribute = attributeMapping.get(attributeName);
        attribute.getCurrentValue().setValue(attribute.getCurrentValue().getValue() - value);
    }

    public void incrAttributeValue(String attributeName, Double value){
        Attribute attribute = attributeMapping.get(attributeName);
        attribute.getCurrentValue().setValue(attribute.getCurrentValue().getValue() + value);
    }


    // 初始化可用属性
    public void initAttributeMapping(List<String> attributeNames) {
        for (String attributeName : attributeNames) {
            Attribute attribute = new Attribute();
            attributeMapping.put(attributeName, attribute);
        }
    }

    // 初始化属性值
    public void initAttributeMappingValue(Map<String, Attribute> attributeMapping){
        attributeMapping.keySet().forEach(key -> {
            Attribute attribute = attributeMapping.get(key);
            attribute.setMaxValue(attributeMapping.get(key).getMaxValue());
            attribute.setCurrentValue(attributeMapping.get(key).getCurrentValue());
        });
    }

    public void linkSkill(List<Event> events){
        this.events.addAll(events);
    }

    public void linkEquipment(List<Equipment> equipments){
        this.equipments.addAll(equipments);
        // 装备的技能挂载到人物上
        equipments.forEach(equipment -> this.events.addAll(equipment.getEvents()));
    }

}
