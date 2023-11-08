package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.dto.UserProfileResponseDto;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationRequestDto;
import com.willyoubackend.domain.user_profile.dto.UserRecommendationResponseDto;
import com.willyoubackend.domain.user_profile.entity.GenderRecommendationEnum;
import com.willyoubackend.domain.user_profile.entity.UserRecommendation;
import com.willyoubackend.domain.user_profile.repository.UserRecommendationRepository;
import com.willyoubackend.global.dto.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRecommendationService {
    private final UserRecommendationRepository userRecommendationRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> createRecommendation(UserEntity user, UserRecommendationRequestDto requestDto) {
        UserRecommendation userRecommendation = new UserRecommendation(requestDto);
        userRecommendation.setUserEntity(user);
        UserRecommendationResponseDto responseDto = new UserRecommendationResponseDto(userRecommendationRepository.save(userRecommendation));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successData(responseDto));
    }

    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> getUserRecommendation(UserEntity user) {
        UserRecommendation userRecommendation = userRecommendationRepository.findByUserEntity(user);
        UserRecommendationResponseDto responseDto = new UserRecommendationResponseDto(userRecommendation);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(responseDto));
    }

    @Transactional
    public ResponseEntity<ApiResponse<UserRecommendationResponseDto>> updateUserRecommendation(UserEntity user, UserRecommendationRequestDto requestDto) {
        UserRecommendation userRecommendation = userRecommendationRepository.findByUserEntity(user);
        userRecommendation.update(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new UserRecommendationResponseDto(userRecommendation)));
    }

    public ResponseEntity<ApiResponse<List<UserProfileResponseDto>>> getRecommendedUsers(UserEntity user) {
        UserRecommendation userRecommendation = userRecommendationRepository.findByUserEntity(user);
        List<UserEntity> userEntityList = userRepository.findAll();
        // Gender
        List<UserEntity> recommendedUserList = genderFilter(userRecommendation, userEntityList);
        // Age
        if (userRecommendation.getRecAge()) {
            recommendedUserList = ageFilter(userRecommendation, user, recommendedUserList);
        }
        // Location
        if (userRecommendation.getRecLocation()) {
            recommendedUserList = distanceFilter(userRecommendation, recommendedUserList);
        }
        List<UserProfileResponseDto> responseDtoList = recommendedUserList.stream().map(UserProfileResponseDto::new).toList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(responseDtoList));
    }

    private List<UserEntity> genderFilter(UserRecommendation userRecommendation, List<UserEntity> userEntityList) {
        List<UserEntity> recommendedUserList = new ArrayList<>();
        if (!userRecommendation.getRecGender().equals(GenderRecommendationEnum.BOTH)) {
            for (UserEntity userEntity : userEntityList) {
                if (userEntity.getUserProfileEntity().getGender().name().equals(userRecommendation.getRecGender().name()))
                    recommendedUserList.add(userEntity);
            }
        } else {
            recommendedUserList = userEntityList;
        }
        return recommendedUserList;
    }

    private List<UserEntity> ageFilter(UserRecommendation userRecommendation, UserEntity user, List<UserEntity> recommendedUserList) {
        List<UserEntity> tempUser = new ArrayList<>();
        Long maxAge = user.getUserProfileEntity().getAge() + userRecommendation.getAgeGap();
        Long minAge = user.getUserProfileEntity().getAge() - userRecommendation.getAgeGap();
        for (UserEntity userEntity : recommendedUserList) {
            if (userEntity.getUserProfileEntity().getAge() >= minAge || userEntity.getUserProfileEntity().getAge() <= maxAge) {
                tempUser.add(userEntity);
            }
        }
        return tempUser;
    }

    private List<UserEntity> distanceFilter(UserRecommendation userRecommendation, List<UserEntity> recommendedUserList) {
        List<UserEntity> tempUser = new ArrayList<>();
        for (UserEntity userEntity : recommendedUserList) {
            double distance = getDistance(
                    userRecommendation.getLatitude(),
                    userRecommendation.getLongitude(),
                    userEntity.getUserRecommendation().getLatitude(),
                    userEntity.getUserRecommendation().getLongitude());
            if (distance < userRecommendation.getDistance()) tempUser.add(userEntity);
        }
        return tempUser;
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c;
    }
}
