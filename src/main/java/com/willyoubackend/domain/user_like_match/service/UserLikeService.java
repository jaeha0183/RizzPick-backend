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
import com.willyoubackend.domain.websocket.entity.ChatRoomRequestDto;
import com.willyoubackend.domain.websocket.repository.ChatRoomRedisRepository;
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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "User Like Service")
public class UserLikeService {

    private final UserRepository userRepository;
    private final UserLikeStatusRepository userLikeStatusRepository;
    private final UserNopeStatusRepository userNopeStatusRepository;
    private final UserMatchStatusRepository userMatchStatusRepository;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatRoomService chatRoomService;
    private final AlertService alertService;
    private final Random random = new Random();

    @Transactional
    public ResponseEntity<ApiResponse<LikeNopeResponseDto>> createLike(UserEntity sentUser, Long userId) {
        UserEntity receivedUser = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );

        if (userLikeStatusRepository.findBySentUserAndReceivedUser(sentUser, receivedUser) != null ||
                userNopeStatusRepository.findBySentUserAndReceivedUser(sentUser, receivedUser) != null)
            throw new CustomException(ErrorCode.DUPLICATED_LIKE);

        alertService.send(receivedUser, sentUser, "새로운 좋아요를 받았습니다.");

        userLikeStatusRepository.save(new UserLikeStatus(sentUser, receivedUser));

        if (userLikeStatusRepository.findBySentUserAndReceivedUser(receivedUser, sentUser) != null) {
            userMatchStatusRepository.save(new UserMatchStatus(sentUser, receivedUser));

            Set<Long> existingRoomIds = new HashSet<>();
            chatRoomRedisRepository.findAll().forEach(chatRoom -> existingRoomIds.add(chatRoom.getId()));

            Long chatRoomId;
            do {
                chatRoomId = 1_000_000L + random.nextInt(9_000_000);
            } while (existingRoomIds.contains(chatRoomId));

            ChatRoomRequestDto chatRoomRequestDto = new ChatRoomRequestDto();
            chatRoomRequestDto.setChatRoomId(chatRoomId);

            chatRoomService.createRoom(chatRoomRequestDto, sentUser, receivedUser);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("행운을 빌어요!"));
    }
}