package xyz.game.util.dealData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseDescriptionExtractor {
    // 替换 Unicode 转义序列
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[一-龥]+");
    // 匹配 HTML 标签的正则表达式
    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]+>");
    // 匹配 attention 标签及其中的数值
    private static final Pattern ATTENTION_PATTERN = Pattern.compile("<attention>([^<]+)</attention>");
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // 属性名称和 ID 的映射关系
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
        put("适应之力", 27);
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
        String outputFilePath = "output.txt";

        try {
            // 解析装备数据并生成接口报文
            List<ObjectNode> equipmentMessages = parseEquipmentData(new File(inputFilePath));
            // 将接口报文保存到文件
            saveMessagesToFile(equipmentMessages, outputFilePath);
            System.out.printf("已将 %d 条接口报文保存到 %s%n", equipmentMessages.size(), outputFilePath);
        } catch (IOException e) {
            System.err.println("发生 IO 异常: " + e.getMessage());
        }
    }

    // ... 已有方法保持不变 ...

    /**
     * 解析装备数据，生成接口报文列表
     * @param jsonFile JSON 文件对象
     * @return 接口报文列表
     * @throws IOException 文件读取异常
     */
    public static List<ObjectNode> parseEquipmentData(File jsonFile) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        List<ObjectNode> equipmentMessages = new ArrayList<>();

        // 假设装备列表在 "data" -> "list" 节点下
        JsonNode equipmentList = rootNode.path("data").path("list");
        if (equipmentList.isArray()) {
            for (JsonNode equipment : equipmentList) {
                ObjectNode message = objectMapper.createObjectNode();
                // 设置装备名称，假设字段名为 "equipmentName"
                message.put("equipmentName", equipment.path("equipmentName").asText());
                // 设置装备图片，假设字段名为 "equipmentImg"
                message.put("equipmentImg", equipment.path("equipmentImg").asText());
                // 设置消耗，假设字段名为 "consumption"
                message.put("consumption", equipment.path("consumption").asInt(1));
                // 设置属性表达式
                message.put("attributeExpression", "notnull");

                ArrayNode equipIds = objectMapper.createArrayNode();
                String description = equipment.path("description").asText();
                // 提取属性值
                Matcher attentionMatcher = ATTENTION_PATTERN.matcher(description);
                String remainingText = description;
                while (attentionMatcher.find()) {
                    String value = attentionMatcher.group(1);
                    int startIndex = attentionMatcher.end();
                    remainingText = remainingText.substring(startIndex);
                    Matcher chineseMatcher = CHINESE_PATTERN.matcher(remainingText);
                    if (chineseMatcher.find()) {
                        String attributeName = chineseMatcher.group();
                        if (ATTRIBUTE_MAP.containsKey(attributeName)) {
                            ObjectNode equipId = objectMapper.createObjectNode();
                            equipId.put("attributeId", ATTRIBUTE_MAP.get(attributeName));
                            equipId.put("attributeName", attributeName);
                            try {
                                double addValue = Double.parseDouble(value);
                                equipId.put("addValue", addValue);
                                equipId.put("multiValue", 0.0);
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
        }
        return equipmentMessages;
    }

    /**
     * 将接口报文保存到文件
     * @param messages 接口报文列表
     * @param outputFilePath 输出文件路径
     * @throws IOException 文件写入异常
     */
    private static void saveMessagesToFile(List<ObjectNode> messages, String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (ObjectNode message : messages) {
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
                writer.write(jsonString);
                writer.newLine();
                writer.newLine();
            }
        }
    }
}