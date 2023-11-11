package com.willyoubackend.domain.user_like_match.service;

import com.willyoubackend.domain.sse.service.AlertService;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_like_match.dto.LikeNopeResponseDto;
import com.willyoubackend.domain.user_like_match.entity.UserLikeStatus;
import com.willyoubackend.domain.user_like_match.entity.UserMatchStatus;
import com.willyoubackend.domain.user_like_match.repository.UserLikeStatusRepository;
import com.willyoubackend.domain.user_like_match.repository.UserMatchStatusRepository;
import com.willyoubackend.domain.user_like_match.repository.UserNopeStatusRepository;
import com.willyoubackend.domain.websocket.repository.ChatRoomRepository;
import com.willyoubackend.domain.websocket.service.ChatRoomService;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "User Like Service")
public class UserLikeService {

    private final UserRepository userRepository;
    private final UserLikeStatusRepository userLikeStatusRepository;
    private final UserNopeStatusRepository userNopeStatusRepository;
    private final UserMatchStatusRepository userMatchStatusRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final AlertService alertService;
    private final Random random = new Random();

    @Transactional
    public ResponseEntity<ApiResponse<LikeNopeResponseDto>> createLike(UserEntity sentUser, Long userId) {
        log.info("좋아요1");
        UserEntity receivedUser = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );
        log.info("좋아요2");
        if (userLikeStatusRepository.findBySentUserAndReceivedUser(sentUser, receivedUser) != null ||
                userNopeStatusRepository.findBySentUserAndReceivedUser(sentUser, receivedUser) != null)
            throw new CustomException(ErrorCode.DUPLICATED_LIKE);
        log.info("좋아요3");
        alertService.send(receivedUser, sentUser, "새로운 좋아요를 받았습니다.");
        log.info("좋아요4");
        userLikeStatusRepository.save(new UserLikeStatus(sentUser, receivedUser));
        log.info("좋아요5");
        if (userLikeStatusRepository.findBySentUserAndReceivedUser(receivedUser, sentUser) != null) {
            log.info("좋아요6");
            alertService.send(receivedUser, sentUser, "새로운 매치가 있습니다.");

            userMatchStatusRepository.save(new UserMatchStatus(sentUser, receivedUser));
            chatRoomService.createRoom(sentUser, receivedUser);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("새로운 인연이 시작 됐습니다!"));
        }
        log.info("좋아요7");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("행운을 빌어요!"));
    }
}