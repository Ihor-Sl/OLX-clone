package ua.sl.ihor.MyOLX.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import ua.sl.ihor.MyOLX.controllers.websockets.ChatWebSocket;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocket chatWebSocket;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocket, "/root")
                .setAllowedOrigins("*");
    }
}
