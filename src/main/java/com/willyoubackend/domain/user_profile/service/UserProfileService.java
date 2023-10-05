package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.dto.UserProfileRequestDto;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.repository.UserProfileRepository;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public void updateUserProfile(Long userId, UserProfileRequestDto userProfileRequestDto) {

        if (userProfileRequestDto.getNickname().isEmpty()){
            throw new CustomException(ErrorCode.INVALID_NICKNAME);
        }

        UserEntity userEntity = findUserById(userId);

        UserProfileEntity userProfileEntity = userEntity.getUserProfileEntity();

        userProfileEntity.updateProfile(userProfileRequestDto);

        userProfileRepository.save(userProfileEntity);
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }
}
