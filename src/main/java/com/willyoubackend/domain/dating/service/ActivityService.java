package com.willyoubackend.domain.dating.service;

import com.willyoubackend.domain.dating.dto.ActivityRequestDto;
import com.willyoubackend.domain.dating.dto.ActivityResponseDto;
import com.willyoubackend.domain.dating.entity.ActivitiesDating;
import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.dating.repository.ActivitiesDatingRepository;
import com.willyoubackend.domain.dating.repository.ActivityRepository;
import com.willyoubackend.domain.dating.repository.DatingRepository;
import com.willyoubackend.domain.user.entity.UserEntity;
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
    private final DatingRepository datingRepository;
    private final ActivityRepository activityRepository;
    private final ActivitiesDatingRepository activitiesDatingRepository;

    public ResponseEntity<ApiResponse<ActivityResponseDto>> activityCreate(UserEntity user, ActivityRequestDto requestDto, Long datingId) {
        Activity activity = new Activity(requestDto);
        log.info("1");
        activity.setUser(user);
        log.info("2");
        ActivityResponseDto responseDto = new ActivityResponseDto(activityRepository.save(activity));
        log.info("3");
        Dating selectedDate = findByIdDateAuthCheck(datingId, user);
        // 배포후 수정
//        if (activitiesDatingRepository.findAllActivitiesDatingByDating(selectedDate).size() == 5)
//            throw new CustomException(ErrorCode.INVALID_ARGUMENT);
        log.info("4");
        ActivitiesDating activitiesDating = new ActivitiesDating(selectedDate, activity);
        activitiesDatingRepository.save(activitiesDating);
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
    public ResponseEntity<ApiResponse<ActivityResponseDto>> updateActivity(UserEntity user, Long id, ActivityRequestDto requestDto) {
        Activity selectedActivity = findByIdActivityAuthCheck(id, user);
        selectedActivity.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new ActivityResponseDto(selectedActivity)));
    }


    @Transactional
    public ResponseEntity<ApiResponse<ActivityResponseDto>> deleteActivity(UserEntity user, Long id) {
        Activity selectedActivity = findByIdActivityAuthCheck(id, user);
        log.info(selectedActivity.getContent());
        ActivitiesDating selectedActivityDating = activitiesDatingRepository.findByActivity(selectedActivity);
        selectedActivity.setDeleteStatus(true);
        selectedActivityDating.setDeleteStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("삭제 되었습니다."));
    }

    private Activity findByIdActivityAuthCheck(Long id, UserEntity user) {
        Activity selectedActivity = activityRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );
        if (!selectedActivity.getUser().getId().equals(user.getId()) || selectedActivity.getDeleteStatus())
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        return selectedActivity;
    }

    private Dating findByIdDateAuthCheck(Long id, UserEntity user) {
        Dating selectedDating = datingRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );
        if (!selectedDating.getUser().getId().equals(user.getId()) || selectedDating.getDeleteStatus())
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        return selectedDating;
    }
}