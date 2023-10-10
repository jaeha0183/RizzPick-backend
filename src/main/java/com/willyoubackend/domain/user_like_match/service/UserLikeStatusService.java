package com.willyoubackend.domain.user_like_match.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.dto.LikeStatusResponseDto;
import com.willyoubackend.domain.user_like_match.entity.UserLikeStatus;
import com.willyoubackend.domain.user_like_match.repository.UserLikeStatusRepository;
import com.willyoubackend.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "User Like Status Service")
public class UserLikeStatusService {
    private final UserLikeStatusRepository userLikeStatusRepository;


    public ResponseEntity<ApiResponse<List<LikeStatusResponseDto>>> getUserLikeStatus(UserEntity sentUser) {
        List<UserLikeStatus> userLikeStatusList = userLikeStatusRepository.findAllBySentUser(sentUser);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(likeStatusResponseDtoList(userLikeStatusList)));
    }

    public ResponseEntity<ApiResponse<List<LikeStatusResponseDto>>> getUserLikedByStatus(UserEntity receivedUser) {
        List<UserLikeStatus> userReceivedLikeStatusList = userLikeStatusRepository.findAllByReceivedUser(receivedUser);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(likeStatusResponseDtoList(userReceivedLikeStatusList)));
    }

    public List<LikeStatusResponseDto> likeStatusResponseDtoList(List<UserLikeStatus> userLikeStatusList) {
        List<String> userEntityList = new ArrayList<>();
        for (UserLikeStatus userLikeStatus : userLikeStatusList) {
            userEntityList.add(userLikeStatus.getReceivedUser().getUsername());
        }
        return userEntityList
                .stream()
                .map(LikeStatusResponseDto::new)
                .toList();
    }
}
