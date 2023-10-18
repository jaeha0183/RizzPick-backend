package com.willyoubackend.domain.user_like_match.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_like_match.dto.LikeNopeResponseDto;
import com.willyoubackend.domain.user_like_match.entity.UserLikeStatus;
import com.willyoubackend.domain.user_like_match.entity.UserMatchStatus;
import com.willyoubackend.domain.user_like_match.repository.UserLikeStatusRepository;
import com.willyoubackend.domain.user_like_match.repository.UserMatchStatusRepository;
import com.willyoubackend.domain.user_like_match.repository.UserNopeStatusRepository;
import com.willyoubackend.domain.user_profile.repository.UserProfileRepository;
import com.willyoubackend.domain.websocket.entity.ChatRoom;
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

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "User Like Service")
public class UserLikeService {

    private final UserRepository userRepository;
    private final UserLikeStatusRepository userLikeStatusRepository;
    private final UserNopeStatusRepository userNopeStatusRepository;
    private final UserMatchStatusRepository userMatchStatusRepository;
    private final UserProfileRepository userProfileRepository;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatRoomService chatRoomService;
    private final Random random = new Random();

    public ResponseEntity<ApiResponse<LikeNopeResponseDto>> createLike(UserEntity sentUser, Long userId) {
        UserEntity receivedUser = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );

        if (userLikeStatusRepository.findBySentUserAndReceivedUser(sentUser, receivedUser) != null ||
                userNopeStatusRepository.findBySentUserAndReceivedUser(sentUser, receivedUser) != null)
            throw new CustomException(ErrorCode.INVALID_ARGUMENT);

        userLikeStatusRepository.save(new UserLikeStatus(sentUser, receivedUser));

        // 매치
        if (userLikeStatusRepository.findBySentUserAndReceivedUser(receivedUser, sentUser) != null) { // 상대방이 나를 좋아요를 눌렀다면
            userMatchStatusRepository.save(new UserMatchStatus(sentUser, receivedUser)); // 매치 상태를 저장한다.
            // 매칭된 사람끼리 채팅방 생성
            // 랜덤한 채팅방 ID 생성
            Long chatRoomId = 1_000_000L + random.nextInt(9_000_000);

            ChatRoomRequestDto chatRoomRequestDto = new ChatRoomRequestDto();
            chatRoomRequestDto.setChatRoomId(chatRoomId); // 생성한 랜덤 채팅방 ID 설정

            chatRoomService.createRoom(chatRoomRequestDto, sentUser, receivedUser); // 채팅방 생성

        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("행운을 빌어요!"));
    }
}