package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.dto.UserProfileRequestDto;
import com.willyoubackend.domain.user_profile.dto.UserProfileResponseDto;
import com.willyoubackend.domain.user_profile.entity.GenderEnum;
import com.willyoubackend.domain.user_profile.entity.LocationEnum;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.repository.UserProfileRepository;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public void updateUserProfile(UserEntity userEntity, UserProfileRequestDto userProfileRequestDto) {

        if (userProfileRequestDto.getNickname().isEmpty()){
            throw new CustomException(ErrorCode.INVALID_NICKNAME);
        }

        UserEntity loggedInUser = findUserById(userEntity.getId());

        UserProfileEntity userProfileEntity = userEntity.getUserProfileEntity();

        userProfileEntity.setUserEntity(loggedInUser);
        userProfileEntity.updateProfile(userProfileRequestDto);

        userProfileRepository.save(userProfileEntity);
    }

    public ResponseEntity<ApiResponse<List<UserProfileResponseDto>>> getUserProfiles(UserEntity userEntity) {

        LocationEnum location = userEntity.getUserProfileEntity().getLocation();
        GenderEnum gender = userEntity.getUserProfileEntity().getGender();

        List<UserProfileResponseDto> userProfileResponseDtoList;

        if(gender == GenderEnum.MALE || gender == GenderEnum.FEMALE) {
            userProfileResponseDtoList = userRepository.findByUserProfileEntity_LocationAndUserProfileEntity_GenderNotAndIdNot(location, gender, userEntity.getId())
                    .stream()
                    .map(UserProfileResponseDto::new)
                    .toList();
        } else {
            userProfileResponseDtoList = userRepository.findByUserProfileEntity_LocationAndIdNot(location, userEntity.getId())
                    .stream()
                    .map(UserProfileResponseDto::new)
                    .toList();
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(userProfileResponseDtoList));
    }

    public ResponseEntity<ApiResponse<UserProfileResponseDto>> getUserProfile(Long userId) {
        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto(findUserById(userId));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(userProfileResponseDto));
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }
}
