package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.dating.repository.DatingRepository;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.dto.SetMainDatingRequestDto;
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
    private final DatingRepository datingRepository;

    public void updateUserProfile(UserEntity userEntity, UserProfileRequestDto userProfileRequestDto) {

        if (userProfileRequestDto.getNickname() == null || userProfileRequestDto.getNickname().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_NICKNAME);
        }

        if (userProfileRequestDto.getGender() == null || userProfileRequestDto.getGender().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_GENDER);
        }

        UserEntity loggedInUser = findUserById(userEntity.getId());

        UserProfileEntity userProfileEntity = userEntity.getUserProfileEntity();

        userProfileEntity.setUserEntity(loggedInUser);
        userProfileEntity.updateProfile(userProfileRequestDto);
        userProfileEntity.setUserActiveStatus(true);

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

    public ResponseEntity<ApiResponse<UserProfileResponseDto>> setMainDating(UserEntity userEntity, SetMainDatingRequestDto setMainDatingRequestDto) {
        Dating dating = findByIdDateAuthCheck(setMainDatingRequestDto.getDatingId(), userEntity);

        UserEntity loggedInUser = findUserById(userEntity.getId());
        UserProfileEntity userProfileEntity = loggedInUser.getUserProfileEntity();

        userProfileEntity.setDating(dating);

        userProfileRepository.save(userProfileEntity);

        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto(loggedInUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(userProfileResponseDto));
    }

    public ResponseEntity<ApiResponse<UserProfileResponseDto>> deleteMainDating(UserEntity userEntity, Long datingId) {
        Dating dating = findByIdDateAuthCheck(datingId, userEntity);

        UserEntity loggedInUser = findUserById(userEntity.getId());
        UserProfileEntity userProfileEntity = loggedInUser.getUserProfileEntity();

        userProfileEntity.setDating(null);

        userProfileRepository.save(userProfileEntity);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("삭제 되었습니다."));
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }

    private Dating findByIdDateAuthCheck(Long id,UserEntity user) {
        Dating selectedDating = datingRepository.findById(id).orElseThrow(
                () ->new CustomException(ErrorCode.NOT_FOUND_ENTITY)
        );
        if (!selectedDating.getUser().getId().equals(user.getId())) throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        return selectedDating;
    }
}
