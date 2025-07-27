package xyz.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import xyz.game.controller.ws.WebRTCSignalingHandler;
import xyz.game.util.RedisUtil;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebRTCSignalingHandler webRTCSignalingHandler;
    public WebSocketConfig(WebRTCSignalingHandler webRTCSignalingHandler) {
        this.webRTCSignalingHandler = webRTCSignalingHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webRTCSignalingHandler, "/ws")
                .setAllowedOrigins("*");
    }
}
