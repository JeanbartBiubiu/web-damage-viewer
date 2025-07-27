package xyz.game.util.dealData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class UploadEquipment {
    private static final String API_URL = "http://127.0.0.1:8888/equipment";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        try {
            // 获取项目根目录
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path imageDir = projectRoot.resolve("images");

            // 读取JSON文件
            ObjectMapper mapper = new ObjectMapper();
            JsonNode equipmentList = mapper.readTree(new File("equipment_output.json"));

            int successCount = 0;
            int totalCount = equipmentList.size();

            // 遍历并上传每个装备
            for (JsonNode equipment : equipmentList) {
                if (uploadEquipment(equipment, imageDir)) {
                    successCount++;
                }
            }

            System.out.printf("\n上传完成。成功上传 %d 个装备，共 %d 个装备。\n",
                    successCount, totalCount);

        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean uploadEquipment(JsonNode equipment, Path imageDir) {
        try {
            String path = "C:\\Users\\Administrator\\Desktop\\loldata\\14.14.1\\img\\item";
            // 获取图片路径并转换为base64
            String imageFileName = equipment.get("equipmentImg").asText();
            File file = new File(path + "\\" + imageFileName);
            if (!file.exists()) {
                System.out.println("图片文件不存在: " + imageFileName);
                return false;
            }
            // 将 File 对象转换为 Path 对象
            Path imagePath = file.toPath();
            // 读取图片文件的所有字节
            // 读取图片并转换为base64
            byte[] imageBytes = Files.readAllBytes(imagePath);
            String base64Image = "data:image/jpeg;base64," +
                    Base64.getEncoder().encodeToString(imageBytes);

            JsonNode equipIds = equipment.get("equipIds");
            // 创建新的JSON对象
            JsonNode equipmentCopy = objectMapper.createObjectNode()
                    .put("equipmentName", equipment.get("equipmentName").asText())
                    .put("equipmentImg", base64Image)
                    .put("consumption", equipment.get("consumption").asInt())
                    .put("attributeExpression", equipment.get("attributeExpression").asText())
                    .set("equipIds", equipIds);

            // 发送POST请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(equipmentCopy.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("成功上传装备: " + equipment.get("equipmentName").asText());
                return true;
            } else {
                System.out.println("上传失败: " + equipment.get("equipmentName").asText() +
                        ", 状态码: " + response.statusCode());
                return false;
            }

        } catch (Exception e) {
            System.err.println("处理装备时发生错误: " + equipment.get("equipmentName").asText());
            e.printStackTrace();
            return false;
        }
    }
}