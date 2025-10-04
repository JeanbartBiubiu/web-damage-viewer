package xyz.game.util.dealData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class getImage {

  public static Map<String,Integer> englishMapId = new HashMap<>();

  /**
   * 创建从name到id的映射
   * @param jsonString JSON字符串
   * @return Map<String, String> 键是name，值是对应的id
   * @throws Exception 如果JSON解析失败
   */
  public static Map<String, String> createNameToIdMap(String jsonString) throws Exception {
    Map<String, String> nameToIdMap = new HashMap<>();

    // 创建Jackson ObjectMapper
    ObjectMapper objectMapper = new ObjectMapper();

    // 解析JSON字符串
    JsonNode rootNode = objectMapper.readTree(jsonString);
    JsonNode dataNode = rootNode.path("data");

    // 遍历data对象中的所有字段
    Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> entry = fields.next();
      String id = entry.getKey(); // 这是英文那串
      JsonNode championNode = entry.getValue();
      String name = championNode.path("title").asText();

      // 将name和id添加到映射中
      nameToIdMap.put(name, id);
    }

    return nameToIdMap;
  }

  public static Map<String, String> getAllEnglishNames(Map<String, String> nameToIdMap) throws Exception {
    Map<String, String> chineseToEnglishMap = new HashMap<>();

    // 创建HTTP客户端
    HttpClient client = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(10))
      .build();

    // 创建HTTP请求
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://damageviewer.xyz/api/indiv"))
      .timeout(Duration.ofSeconds(10))
      .header("Content-Type", "application/json")
      .GET()
      .build();

    // 发送请求并获取响应
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // 检查响应状态码
    if (response.statusCode() != 200) {
      throw new RuntimeException("HTTP请求失败，状态码: " + response.statusCode());
    }

    // 解析JSON响应
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(response.body());
    JsonNode dataNode = rootNode.path("data");
    JsonNode listNode = dataNode.path("list");

    // 创建id到英文名的映射
    Map<String, String> idToEnglishMap = new HashMap<>();
    for (JsonNode itemNode : listNode) {
      String indivId = String.valueOf(itemNode.path("indivId").asInt());
      String indivName = itemNode.path("indivName").asText();
      idToEnglishMap.put(indivId, indivName);
    }

    // 遍历nameToIdMap，将中文名映射到英文名
    for (Map.Entry<String, String> entry : nameToIdMap.entrySet()) {
      String chineseName = entry.getKey();
      String id = entry.getValue();
      String englishName = idToEnglishMap.get(id);
      if (englishName != null) {
        chineseToEnglishMap.put(chineseName, englishName);
      }
    }

    return chineseToEnglishMap;
  }

  /**
   * 获取所有英雄的中文名到英文名的映射（完整流程）
   * @param championJson 英雄数据的JSON字符串
   * @return Map<String, String> 键是中文名，值是对应的英文名
   * @throws Exception 如果HTTP请求或JSON解析失败
   */
  public static Map<String, String> getAllEnglishNames(String championJson) throws Exception {
    // 创建name到id的映射
    Map<String, String> nameToIdMap = createNameToIdMap(championJson);

    // 获取所有英文名
    return getAllEnglishNames(nameToIdMap);
  }

  /**
   * 获取英雄的英文名
   * @param chineseName 中文名
   * @param nameToIdMap 中文名到id的映射
   * @return 英文名，如果找不到则返回null
   * @throws Exception 如果HTTP请求或JSON解析失败
   */
  public static String getEnglishName(String chineseName, Map<String, String> nameToIdMap) throws Exception {
    // 获取所有英雄的中文名到英文名的映射
    Map<String, String> allEnglishNames = getAllEnglishNames(nameToIdMap);

    // 返回指定英雄的英文名
    return allEnglishNames.get(chineseName);
  }

  /**
   * 获取英雄的英文名（完整流程）
   * @param chineseName 中文名
   * @param championJson 英雄数据的JSON字符串
   * @return 英文名，如果找不到则返回null
   * @throws Exception 如果HTTP请求或JSON解析失败
   */
  public static String getEnglishName(String chineseName, String championJson) throws Exception {
    // 获取所有英雄的中文名到英文名的映射
    Map<String, String> allEnglishNames = getAllEnglishNames(championJson);

    // 返回指定英雄的英文名
    return allEnglishNames.get(chineseName);
  }

  static void main() {
    File file = new File("C:\\Users\\Administrator\\Desktop\\loldata\\champion.json");
    try (FileReader reader = new FileReader(file)) {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(reader);
      String championJson = rootNode.toString();
      Map<String, String> nameToIdMap = createNameToIdMap(championJson);
      uploadImages(nameToIdMap);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void uploadImages(Map<String, String> nameToIdMap) throws Exception {
      Map<String, String> chineseToEnglishMap = new HashMap<>();

      // 创建HTTP客户端
      HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

      // 创建HTTP请求
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://damageviewer.xyz/api/indiv"))
        .timeout(Duration.ofSeconds(10))
        .header("Content-Type", "application/json")
        .GET()
        .build();

      // 发送请求并获取响应
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      // 检查响应状态码
      if (response.statusCode() != 200) {
        throw new RuntimeException("HTTP请求失败，状态码: " + response.statusCode());
      }

      // 解析JSON响应
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(response.body());
      JsonNode dataNode = rootNode.path("data");
      JsonNode listNode = dataNode.path("list");

      // 创建id到英文名的映射
      Map<String, String> idToEnglishMap = new HashMap<>();
      for (JsonNode itemNode : listNode) {
        String indivId = String.valueOf(itemNode.path("indivId").asInt());
        String indivName = itemNode.path("indivName").asText();
        System.out.println(indivId);
        System.out.println(indivName);
        System.out.println(nameToIdMap.get(indivName));
        String base64 = imageToBase64("C:\\Users\\Administrator\\Desktop\\loldata\\champion\\" + nameToIdMap.get(indivName) + ".png");
        System.out.println(base64);
        String s = uploadImage(Integer.parseInt(indivId), base64);
        System.out.println(s);
      }
  }

  public static String imageToBase64(String imagePath) throws IOException {
    return imageToBase64(imagePath, 64, 64); // 默认压缩为64x64
  }

  /**
   * 将图片文件转换为base64编码字符串，并可以指定压缩尺寸
   * @param imagePath 图片文件路径
   * @param targetWidth 目标宽度
   * @param targetHeight 目标高度
   * @return 图片的base64编码字符串
   * @throws IOException 如果文件读取失败
   */
  public static String imageToBase64(String imagePath, int targetWidth, int targetHeight) throws IOException {
    File imageFile = new File(imagePath);
    if (!imageFile.exists()) {
      throw new IOException("图片文件不存在: " + imagePath);
    }

    // 读取原始图片
    BufferedImage originalImage = ImageIO.read(imageFile);
    if (originalImage == null) {
      throw new IOException("无法读取图片文件: " + imagePath);
    }

    // 创建压缩后的图片
    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = resizedImage.createGraphics();
    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
    graphics2D.dispose();

    // 将压缩后的图片转换为字节数组
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(resizedImage, "png", byteArrayOutputStream);
    byte[] imageBytes = byteArrayOutputStream.toByteArray();
    byteArrayOutputStream.close();

    // 将字节数组转换为base64编码
    String base64String = Base64.getEncoder().encodeToString(imageBytes);

    // 添加data URI前缀，格式为：data:image/<格式>;base64,<base64编码数据>
    return "data:image/png;base64," + base64String;
  }

  /**
   * 上传图片到指定接口
   * @param indivId 数字id
   * @param imageBase64 图片的base64格式（可以为空字符串）
   * @return 接口响应的JSON字符串
   * @throws Exception 如果HTTP请求或JSON解析失败
   */
  public static String uploadImage(int indivId, String imageBase64) throws Exception {
    // 创建HTTP客户端
    HttpClient client = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(30)) // 上传图片可能需要更长时间
      .build();

    // 创建JSON请求体
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode requestBody = objectMapper.createObjectNode();
    requestBody.put("uri", "individual_" + indivId);
    requestBody.put("image", imageBase64); // 可以为空字符串

    // 创建HTTP请求
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://damageviewer.xyz/api/image"))
      .timeout(Duration.ofSeconds(30))
      .header("Content-Type", "application/json")
      .header("Currentschema", "default_table")
      .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
      .build();

    // 发送请求并获取响应
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // 检查响应状态码
    if (response.statusCode() != 200) {
      throw new RuntimeException("HTTP请求失败，状态码: " + response.statusCode() + "，响应内容: " + response.body());
    }

    return response.body();
  }
}
