package com.willyoubackend.domain.user_like_match.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_like_match.dto.LikeNopeResponseDto;
import com.willyoubackend.domain.user_like_match.entity.UserNopeStatus;
import com.willyoubackend.domain.user_like_match.repository.UserLikeStatusRepository;
import com.willyoubackend.domain.user_like_match.repository.UserNopeStatusRepository;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "User Like Service")
public class UserNopeService {
    private final UserRepository userRepository;
    private final UserLikeStatusRepository userLikeStatusRepository;
    private final UserNopeStatusRepository userNopeStatusRepository;

    public ResponseEntity<ApiResponse<LikeNopeResponseDto>> createNope(UserEntity sentUser, Long userId) {
        UserEntity receivedUser = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );
        if (userLikeStatusRepository.findBySentUserAndReceivedUser(sentUser, receivedUser) != null ||
                userNopeStatusRepository.findBySentUserAndReceivedUser(sentUser, receivedUser) != null)
            throw new CustomException(ErrorCode.INVALID_ARGUMENT);
        userNopeStatusRepository.save(new UserNopeStatus(sentUser, receivedUser));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("진심이에요?"));
    }
}