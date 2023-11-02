package com.willyoubackend.domain.dating.service;

import com.willyoubackend.domain.dating.dto.ActivityResponseDto;
import com.willyoubackend.domain.dating.dto.DatingDetailResponseDto;
import com.willyoubackend.domain.dating.dto.DatingRequestDto;
import com.willyoubackend.domain.dating.dto.DatingResponseDto;
import com.willyoubackend.domain.dating.entity.ActivitiesDating;
import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.dating.repository.ActivitiesDatingRepository;
import com.willyoubackend.domain.dating.repository.DatingRepository;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Dating Service")
public class DatingService {
    private final DatingRepository datingRepository;
    private final ActivitiesDatingRepository activitiesDatingRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<DatingResponseDto>> createDating(UserEntity user) {
        // 배포시 변경
        if (datingRepository.findAllByUserOrderByCreatedAt(user).size() == 5)
            throw new CustomException(ErrorCode.INVALID_ARGUMENT);
        Dating dating = new Dating(
                "이목을 끄는 이름을 지어주세요!",
                "어디서 만나실건가요?",
                "어떤 컨셉의 데이트인가요?");
        dating.setUser(user);
        DatingResponseDto responseDto = new DatingResponseDto(datingRepository.save(dating));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successData(responseDto));
    }

    public ResponseEntity<ApiResponse<List<DatingResponseDto>>> getDatingList(UserEntity user) {
        List<DatingResponseDto> datingResponseDtoList = datingRepository.findAllByOrderByCreatedAt(user)
                .stream()
                .map(DatingResponseDto::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(datingResponseDtoList));
    }

    public ResponseEntity<ApiResponse<List<DatingResponseDto>>> getDatingListByUser(UserEntity user) {
        List<DatingResponseDto> datingResponseDtoListByUser = datingRepository.findAllByUserOrderByCreatedAt(user)
                .stream()
                .map(DatingResponseDto::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(datingResponseDtoListByUser));
    }

    public ResponseEntity<ApiResponse<List<DatingResponseDto>>> getDatingListByLocation(String location) {
        List<DatingResponseDto> datingResponseDtoListByLocation = datingRepository.findAllByLocationOrderByCreatedAt(location)
                .stream()
                .map(DatingResponseDto::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(datingResponseDtoListByLocation));
    }

    public ResponseEntity<ApiResponse<List<DatingResponseDto>>> getDatingListBySelectedUser(Long userId) {
        UserEntity selectedUser = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );
        List<DatingResponseDto> datingResponseDtoList = datingRepository.findAllByUser(selectedUser)
                .stream()
                .map(DatingResponseDto::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(datingResponseDtoList));
    }

    public ResponseEntity<ApiResponse<DatingDetailResponseDto>> getDatingDetail(Long id) {
        Dating selectedDating = datingRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ENTITY));
        List<Activity> activityList = new ArrayList<>();
        for (ActivitiesDating activitiesDating : activitiesDatingRepository.findAllActivitiesDatingByDating(selectedDating)) {
            activityList.add(activitiesDating.getActivity());
        }
        List<ActivityResponseDto> selectedActivities = activityList
                .stream()
                .map(ActivityResponseDto::new)
                .toList();
        DatingDetailResponseDto responseDto = new DatingDetailResponseDto(selectedDating, selectedActivities);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(responseDto));
    }

    @Transactional
    public ResponseEntity<ApiResponse<DatingResponseDto>> updateDating(UserEntity user, Long id, DatingRequestDto requestDto) {
        Dating selectedDate = findByIdDateAuthCheck(id, user);
        selectedDate.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new DatingResponseDto(selectedDate)));
    }

    @Transactional
    public ResponseEntity<ApiResponse<DatingResponseDto>> deleteDating(UserEntity user, Long id) {
        Dating selectedDate = findByIdDateAuthCheck(id, user);
        List<ActivitiesDating> activitiesDatingList = activitiesDatingRepository.findAllActivitiesDatingByDating(selectedDate);
        selectedDate.setDeleteStatus(true);
        for (ActivitiesDating activitiesDating : activitiesDatingList) {
            activitiesDating.setDeleteStatus(true);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("삭제 되었습니다."));
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