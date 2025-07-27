package xyz.game.controller.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 不需要redis，只匹配本机的，其他机器匹配不到拉倒
 */
@Component
public class WebRTCSignalingHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> rooms = new ConcurrentHashMap<>(); // roomId -> sessionIds
    private final Map<String, String> sessionToRoom = new ConcurrentHashMap<>(); // sessionId -> roomId
    private final ObjectMapper mapper = new ObjectMapper();

    // 添加获取 roomId 列表的方法
    public Set<String> getRoomIds() {
        return rooms.keySet();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        // 向客户端返回sessionId
        ObjectNode msg = mapper.createObjectNode();
        msg.put("type", "sessionId");
        msg.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(msg.toString()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode node = mapper.readTree(message.getPayload());
        String type = node.get("type").asText();

        if ("join".equals(type)) {
            String roomId = node.get("roomId").asText();
            if (rooms.keySet().size()>10000){
                // 粗暴的避免内存溢出
                return;
            }
            rooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
            rooms.get(roomId).add(session.getId());
            sessionToRoom.put(session.getId(), roomId);

            // 通知房间中已有成员
            for (String sid : rooms.get(roomId)) {
                if (!sid.equals(session.getId())) {
                    ObjectNode notify = mapper.createObjectNode();
                    notify.put("type", "peer-joined");
                    notify.put("peerId", session.getId());
                    sessions.get(sid).sendMessage(new TextMessage(notify.toString()));
                }
            }
        } else if ("signal".equals(type)) {
            String targetId = node.get("to").asText();
            JsonNode data = node.get("data");

            WebSocketSession targetSession = sessions.get(targetId);
            if (targetSession != null && targetSession.isOpen()) {
                ObjectNode relay = mapper.createObjectNode();
                relay.put("type", "signal");
                relay.put("from", session.getId());
                relay.set("data", data);
                targetSession.sendMessage(new TextMessage(relay.toString()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        String roomId = sessionToRoom.remove(session.getId());
        if (roomId != null) {
            Set<String> set = rooms.get(roomId);
            if (set != null) {
                set.remove(session.getId());
                for (String sid : set) {
                    ObjectNode notify = mapper.createObjectNode();
                    notify.put("type", "peer-left");
                    notify.put("peerId", session.getId());
                    sessions.get(sid).sendMessage(new TextMessage(notify.toString()));
                }
                if (set.isEmpty()) {
                    rooms.remove(roomId);
                }
            }
        }
    }
}
