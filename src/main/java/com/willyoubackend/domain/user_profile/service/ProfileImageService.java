package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.dto.ImageResponseDto;
import com.willyoubackend.domain.user_profile.dto.ProfileImageRequestDto;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.repository.ProfileImageRepository;
import com.willyoubackend.domain.user_profile.repository.UserProfileRepository;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import com.willyoubackend.global.util.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j(topic = "PROFILE_IMAGE_SERVICE")
@RequiredArgsConstructor
public class ProfileImageService {

    private final S3Uploader s3Uploader;
    private final ProfileImageRepository profileImageRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<ApiResponse<ImageResponseDto>> updateProfileImage(UserEntity userEntity, ProfileImageRequestDto profileImageRequestDto) throws IOException {
        ProfileImageEntity profileImageEntity = null;

        switch (profileImageRequestDto.getAction()) {
            case ADD -> profileImageEntity = addProfileImage(userEntity, profileImageRequestDto);
            case DELETE -> {
                profileImageEntity = findImageById(profileImageRequestDto.getId());
                // 사용자가 이미지 소유자인지 확인
                validateUserImageOwnership(userEntity, profileImageEntity);
                deleteProfileImage(profileImageRequestDto.getId());
            }
            case MODIFY -> {
                profileImageEntity = findImageById(profileImageRequestDto.getId());
                // 사용자가 이미지 소유자인지 확인
                validateUserImageOwnership(userEntity, profileImageEntity);
                profileImageEntity = modifyProfileImage(userEntity, profileImageRequestDto);
            }
        }

        updateUserActiveStatus(userEntity);

        ImageResponseDto imageResponseDto = (profileImageEntity != null) ? new ImageResponseDto(profileImageEntity) : null;
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(imageResponseDto));
    }

    private void validateUserImageOwnership(UserEntity userEntity, ProfileImageEntity profileImageEntity) {
        log.info("Validating image ownership: userEntity ID = {}, profileImageEntity user ID = {}", userEntity.getId(), profileImageEntity.getUserEntity().getId());
        if (!profileImageEntity.getUserEntity().getId().equals(userEntity.getId())) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }
    }

    private void updateUserActiveStatus(UserEntity userEntity) {
        UserProfileEntity userProfileEntity = userEntity.getUserProfileEntity();

        boolean isActive = userProfileEntity.getNickname() != null && !userProfileEntity.getNickname().isEmpty() &&
                userProfileEntity.getGender() != null;

        userProfileEntity.setUserActiveStatus(isActive);
        userProfileRepository.save(userProfileEntity);
    }

    private ProfileImageEntity addProfileImage(UserEntity userEntity, ProfileImageRequestDto profileImageRequestDto) throws IOException {
        List<ProfileImageEntity> profileImageEntities = profileImageRepository.findAllByUserEntity(userEntity);
        if (profileImageEntities.size() >= 6) {
            throw new CustomException(ErrorCode.INVALID_MAXIMA);
        }
        String fileName = s3Uploader.upload(profileImageRequestDto.getImage(), "profileImage/" + userEntity.getUsername());
        ProfileImageEntity profileImageEntity = new ProfileImageEntity(fileName);
        profileImageEntity.setUserEntity(userEntity);
        profileImageRepository.save(profileImageEntity);
        return profileImageEntity;
    }

    public void deleteProfileImage(Long imageId) {
        ProfileImageEntity profileImageEntity = findImageById(imageId);
        UserEntity userEntity = profileImageEntity.getUserEntity();
        List<ProfileImageEntity> profileImageEntities = profileImageRepository.findAllByUserEntity(userEntity);
//        if (profileImageEntities.size() <= 1) {
//            throw new CustomException(ErrorCode.INVALID_IMAGE);
//        }
        s3Uploader.delete(profileImageEntity.getImage());
        profileImageRepository.deleteById(imageId);
        // Jwywoo
    }

    private ProfileImageEntity modifyProfileImage(UserEntity userEntity, ProfileImageRequestDto profileImageRequestDto) throws IOException {
        ProfileImageEntity profileImageEntity = findImageById(profileImageRequestDto.getId());
        s3Uploader.delete(profileImageEntity.getImage());
        String fileName = s3Uploader.upload(profileImageRequestDto.getImage(), "profileImage/" + userEntity.getUsername());
        profileImageEntity.setImage(fileName);
        profileImageRepository.save(profileImageEntity);
        return profileImageEntity;
    }

    private ProfileImageEntity findImageById(Long imageId) {
        return profileImageRepository.findById(imageId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }

    @Transactional
    public ResponseEntity<ApiResponse<ImageResponseDto>> updateProfileImage(Long userId, ProfileImageRequestDto profileImageRequestDto) throws IOException {
        // userId를 이용하여 UserEntity 객체를 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기존 로직 실행
        ProfileImageEntity profileImageEntity = null;

        switch (profileImageRequestDto.getAction()) {
            case ADD -> profileImageEntity = addProfileImage(userEntity, profileImageRequestDto);
            case DELETE -> deleteProfileImage(profileImageRequestDto.getId());
            case MODIFY -> profileImageEntity = modifyProfileImage(userEntity, profileImageRequestDto);
        }

        updateUserActiveStatus(userEntity);

        ImageResponseDto imageResponseDto = (profileImageEntity != null) ? new ImageResponseDto(profileImageEntity) : null;
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(imageResponseDto));
    }
}