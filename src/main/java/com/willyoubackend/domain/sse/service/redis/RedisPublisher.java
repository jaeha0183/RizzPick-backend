package com.willyoubackend.domain.sse.service.redis;

import com.willyoubackend.domain.sse.dto.AlertResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j(topic = "RedisPublisher")
@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publishAlert(ChannelTopic topic, AlertResponseDto alertResponseDto) {
        redisTemplate.convertAndSend(topic.getTopic(), alertResponseDto);
        log.info("Publish Redis topic : {}", topic.getTopic());
    }
}
