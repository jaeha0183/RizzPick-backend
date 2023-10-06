package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.dto.UserProfileRequestDto;
import com.willyoubackend.domain.user_profile.dto.UserProfileResponseDto;
import com.willyoubackend.domain.user_profile.entity.GenderEnum;
import com.willyoubackend.domain.user_profile.entity.LocationEnum;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.repository.UserProfileRepository;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
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

    public List<UserProfileResponseDto> getUserProfiles(UserEntity userEntity) {

        LocationEnum location = userEntity.getUserProfileEntity().getLocation();
        GenderEnum gender = userEntity.getUserProfileEntity().getGender();

        List<UserEntity> userEntities;

        if(gender == GenderEnum.MALE || gender == GenderEnum.FEMALE) {
            userEntities = userRepository.findByUserProfileEntity_LocationAndUserProfileEntity_GenderNotAndIdNot(location, gender, userEntity.getId());
        } else {
            userEntities = userRepository.findByUserProfileEntity_LocationAndIdNot(location, userEntity.getId());
        }

        return userEntities.stream().map(UserProfileResponseDto::new).collect(Collectors.toList());
    }

    public UserProfileResponseDto getUserProfile(UserEntity userEntity, Long userId) {
        UserEntity targetUser = findUserById(userId);

        return new UserProfileResponseDto(targetUser);
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }
}
