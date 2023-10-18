package com.willyoubackend.domain.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    private final StompHandler stompHandler;

    // 1. 클라이언트가 웹 소켓 서버에 연결하는 데 사용할 웹 소켓 엔드포인트.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // endpoint 설정 : handshake 할 경로
        registry.addEndpoint("/chatroom")
                // CORS 설정
                .setAllowedOriginPatterns("*");
        // SockJS 사용
//                .withSockJS();
    }

    // 2. 한 클라이언트에서 다른 클라이언트로 메시지를 라우팅하는 데 사용될 메시지 브로커를 구성.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /app 으로 시작하는 모든 메시지는 @MessageMapping 어노테이션이 달린 메서드로 라우팅.
        registry.setApplicationDestinationPrefixes("/app"); // pub
        // "/topic"으로 시작하는 메시지가 메시지 브로커로 라우팅되도록 정의. 메시지브로커는 해당 채팅방을 구독하고 있는 클라이언트에게 메시지 전달
        registry.enableSimpleBroker("/topic", "/queue"); // sub
    }
}
