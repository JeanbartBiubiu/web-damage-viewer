package xyz.game.util.dealData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateEquipmentImages {
    public static void main(String[] args) {
        try {
            // 读取JSON文件
            ObjectMapper mapper = new ObjectMapper();
            JsonNode itemData = mapper.readTree(new File("C:\\project\\damageViewer\\dataApi\\src\\main\\resources\\item.json"));
            JsonNode equipmentData = mapper.readTree(new File("equipment_output.json"));

            // 获取item.json中的data节点
            JsonNode items = itemData.get("data");

            // 遍历equipment_output.json中的每个装备
            for (JsonNode equipment : equipmentData) {
                String equipmentName = equipment.get("equipmentName").asText();

                // 在item.json中查找匹配的装备
                for (JsonNode item : items) {
                    if (item.get("name").asText().equals(equipmentName)) {
                        // 获取图片信息
                        JsonNode image = item.get("image");
                        if (image != null) {
                            String imagePath = image.get("full").asText();
                            // 更新equipment_output.json中的图片路径
                            ((ObjectNode) equipment).put("equipmentImg", imagePath);
                        }
                        break;
                    }
                }
            }

            // 将更新后的数据写回文件
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("equipment_output.json"), equipmentData);

            System.out.println("装备图片信息更新完成！");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}