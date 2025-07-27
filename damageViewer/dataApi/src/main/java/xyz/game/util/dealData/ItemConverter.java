 package xyz.game.util.dealData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemConverter {
    private static final Pattern ATTENTION_PATTERN = Pattern.compile("<attention>([^<]+)</attention>");
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[一-龥]+");
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // 属性名称和ID的映射关系
    private static final Map<String, Integer> ATTRIBUTE_MAP = new HashMap<>() {{
        put("攻击力", 1);
        put("攻速", 2);
        put("移速", 3);
        put("生命值", 4);
        put("护甲", 5);
        put("魔抗", 6);
        put("法力值", 7);
        put("生命回复", 8);
        put("法力回复", 9);
        put("攻击范围", 10);
        put("法术强度", 12);
        put("暴击几率", 13);
        put("暴击伤害", 14);
        put("生命偷取", 15);
        put("全能吸血", 16);
        put("技能急速", 17);
        put("基础技能急速", 18);
        put("终极技能急速", 19);
        put("适应之力", 20);
        put("穿甲", 21);
        put("护甲穿透", 22);
        put("法穿", 23);
        put("法术穿透", 24);
        put("治疗和护盾强度", 25);
        put("韧性", 26);
        put("金币", 28);
        put("经验值", 29);
    }};

    public static void main(String[] args) {
        String inputFilePath = "C:\\project\\damageViewer\\dataApi\\src\\main\\resources\\item.json";
        String outputFilePath = "equipment_output.json";

        try {
            List<ObjectNode> equipmentMessages = convertItems(new File(inputFilePath));
            saveToFile(equipmentMessages, outputFilePath);
            System.out.println("转换完成，共处理 " + equipmentMessages.size() + " 个装备");
        } catch (IOException e) {
            System.err.println("处理过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<ObjectNode> convertItems(File jsonFile) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        List<ObjectNode> equipmentMessages = new ArrayList<>();
        
        JsonNode dataNode = rootNode.path("data");
        Iterator<String> itemIds = dataNode.fieldNames();
        
        while (itemIds.hasNext()) {
            String itemId = itemIds.next();
            JsonNode item = dataNode.get(itemId);
            
            ObjectNode message = objectMapper.createObjectNode();
            message.put("equipmentName", item.path("name").asText());
            message.put("equipmentImg", ""); // 图片base64需要另外处理
            message.put("consumption", 1);
            message.put("attributeExpression", "notnull");
            
            ArrayNode equipIds = objectMapper.createArrayNode();
            String description = item.path("description").asText();
            
            // 提取属性值
            Matcher attentionMatcher = ATTENTION_PATTERN.matcher(description);
            String remainingText = description;
            
            while (attentionMatcher.find()) {
                String value = attentionMatcher.group(1);
                int startIndex = attentionMatcher.end();
                if (startIndex >= remainingText.length()) {
                    break;
                }
                remainingText = remainingText.substring(startIndex);
                
                Matcher chineseMatcher = CHINESE_PATTERN.matcher(remainingText);
                if (chineseMatcher.find()) {
                    String attributeName = chineseMatcher.group();
                    if (ATTRIBUTE_MAP.containsKey(attributeName)) {
                        ObjectNode equipId = objectMapper.createObjectNode();
                        equipId.put("attributeId", ATTRIBUTE_MAP.get(attributeName));
                        equipId.put("attributeName", attributeName);
                        
                        // 处理数值
                        try {
                            // 移除百分号并转换为小数
                            String cleanValue = value.replace("%", "");
                            double numericValue = Double.parseDouble(cleanValue);
                            
                            if (value.contains("%")) {
                                equipId.put("addValue", 0.0);
                                equipId.put("multiValue", numericValue / 100.0);
                            } else {
                                equipId.put("addValue", numericValue);
                                equipId.put("multiValue", 0.0);
                            }
                        } catch (NumberFormatException e) {
                            equipId.put("addValue", 0.0);
                            equipId.put("multiValue", 0.0);
                        }
                        
                        equipIds.add(equipId);
                    }
                }
            }
            
            message.set("equipIds", equipIds);
            equipmentMessages.add(message);
        }
        
        return equipmentMessages;
    }

    private static void saveToFile(List<ObjectNode> messages, String outputFilePath) throws IOException {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(messages);
            writer.write(jsonString);
        }
    }
}