package com.willyoubackend.domain.sse.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.willyoubackend.domain.sse.dto.AlertResponseDto;
import com.willyoubackend.domain.sse.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j(topic = "RedisSubscriber")
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmitterRepository emitterRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            log.info("Receive Redis Message : {}", publishMessage);
            AlertResponseDto alertResponseDto = objectMapper.readValue(publishMessage, AlertResponseDto.class);

            String userId = String.valueOf(alertResponseDto.getReceiver().getId());
            Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(userId);

            sseEmitters.forEach((id, emitter)->{
                try {
                    emitter.send(SseEmitter.event().name("Redis").data(alertResponseDto));
                } catch (IOException e) {
                    emitter.complete();
                    emitterRepository.deleteById(id);
                }
            });

        } catch (Exception e) {
            log.error("Parsing Error : {}", e.getMessage(), e);
            throw new IllegalArgumentException("파싱 에러");
        }
    }
}
