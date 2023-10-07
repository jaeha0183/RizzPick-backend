package com.willyoubackend.domain.dating.service;

import com.willyoubackend.domain.dating.dto.ActivityRequestDto;
import com.willyoubackend.domain.dating.dto.ActivityResponseDto;
import com.willyoubackend.domain.dating.dto.DatingResponseDto;
import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.dating.repository.ActivityRepository;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Activity Service")
public class ActivityService {
    private final ActivityRepository activityRepository;
    public ResponseEntity<ApiResponse<ActivityResponseDto>> activityCreate(UserEntity user, ActivityRequestDto requestDto) {
        Activity activity = new Activity(requestDto);
        activity.setUser(user);
        ActivityResponseDto responseDto = new ActivityResponseDto(activityRepository.save(activity));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successData(responseDto));
    }

    public ResponseEntity<ApiResponse<List<ActivityResponseDto>>> getActivityList() {
        List<ActivityResponseDto> activityResponseDtoList = activityRepository.findAllByOrderByCreatedAt()
                .stream()
                .map(ActivityResponseDto::new)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successData(activityResponseDtoList));
    }

    public ResponseEntity<ApiResponse<List<ActivityResponseDto>>> getActivityListByUser(UserEntity user) {
        List<ActivityResponseDto> activityResponseDtoList = activityRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(ActivityResponseDto::new)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successData(activityResponseDtoList));
    }

    @Transactional
    public ResponseEntity<ApiResponse<ActivityResponseDto>> updateDating(UserEntity user, Long id, ActivityRequestDto requestDto) {
        Activity selectedDate = findByIdActivity(id);
        if (!selectedDate.getUser().getId().equals(user.getId())) throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        selectedDate.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new ActivityResponseDto(selectedDate)));
    }

    public ResponseEntity<ApiResponse<ActivityResponseDto>> deleteDating(UserEntity user, Long id) {
        Activity selectedActivity = findByIdActivity(id);
        if (!selectedActivity.getUser().getId().equals(user.getId())) throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        activityRepository.delete(selectedActivity);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("삭제 되었습니다."));
    }

    private Activity findByIdActivity(Long id) {
        return activityRepository.findById(id).orElseThrow(
                () ->new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );
    }
}
