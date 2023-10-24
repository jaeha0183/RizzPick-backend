package com.willyoubackend.domain.sse.service;

import com.willyoubackend.domain.sse.dto.AlertResponseDto;
import com.willyoubackend.domain.sse.entity.Alert;
import com.willyoubackend.domain.sse.repository.AlertRepository;
import com.willyoubackend.domain.sse.repository.EmitterRepository;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.global.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "Alert Service")
@RequiredArgsConstructor
public class AlertService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final AlertRepository alertRepository;
    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(UserDetailsImpl userDetails, String lastEventId) {
        Long userId = userDetails.getUser().getId();
        String id = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 503 에러 방지용 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [userId =" + userId + "]");

        if (lastEventId != null && !lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류");
        }
    }

    public void send(UserEntity receiver, UserEntity sender, String message){
        Alert alert = createAlert(receiver, sender, message);
        String id = String.valueOf(receiver.getId());
        alertRepository.save(alert);
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach((key, emitter) -> {
            emitterRepository.saveEventCache(key, alert);
            sendToClient(emitter, key, new AlertResponseDto(alert));
        });
    }

    private Alert createAlert(UserEntity receiver, UserEntity sender, String message){
        return Alert.builder()
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .url("/userProfile/" + sender.getId())
                .readStatus(false)
                .build();
    }

    public ResponseEntity<ApiResponse<List<AlertResponseDto>>> getAlerts(UserEntity user) {
        List<Alert> alerts = alertRepository.findByReceiver(user);
        List<AlertResponseDto> alertResponseDtos = alerts.stream()
                .map(AlertResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(alertResponseDtos));
    }

    public ResponseEntity<ApiResponse<AlertResponseDto>> readAlert(Long id) {
        Alert alert = alertRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("존재하지 않는 알림입니다."));
        alert.read();
        AlertResponseDto alertResponseDto = new AlertResponseDto(alert);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(alertResponseDto));
    }
}
