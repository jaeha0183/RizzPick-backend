package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.dto.ProfileImageRequestDto;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.repository.ProfileImageRepository;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import com.willyoubackend.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final S3Uploader s3Uploader;
    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;


    public void updateProfileImage(UserEntity userEntity, ProfileImageRequestDto profileImageRequestDto) throws IOException {


        switch (profileImageRequestDto.getAction()) {
            case ADD -> addProfileImage(userEntity, profileImageRequestDto);
            case DELETE -> deleteProfileImage(profileImageRequestDto.getId());
            case MODIFY -> modifyProfileImage(userEntity, profileImageRequestDto);
        }

        List<ProfileImageEntity> profileImageEntities = profileImageRepository.findAllByUserEntity(userEntity);

        if (profileImageEntities.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_IMAGE);
        }
    }

    private void addProfileImage(UserEntity userEntity, ProfileImageRequestDto profileImageRequestDto) throws IOException {
        List<ProfileImageEntity> profileImageEntities = profileImageRepository.findAllByUserEntity(userEntity);
        if (profileImageEntities.size() >= 6) {
            throw new CustomException(ErrorCode.INVALID_MAXIMA);
        }
        String fileName = s3Uploader.upload(profileImageRequestDto.getImage(), "profileImage/" + userEntity.getUsername());
        ProfileImageEntity profileImageEntity = new ProfileImageEntity(fileName);
        profileImageEntity.setUserEntity(userEntity);
        profileImageRepository.save(profileImageEntity);
    }

    private void deleteProfileImage(Long imageId) {
        ProfileImageEntity profileImageEntity = findImageById(imageId);
        UserEntity userEntity = profileImageEntity.getUserEntity();
        List<ProfileImageEntity> profileImageEntities = profileImageRepository.findAllByUserEntity(userEntity);
        if (profileImageEntities.size() <= 1) {
            throw new CustomException(ErrorCode.INVALID_IMAGE);
        }
        s3Uploader.delete(profileImageEntity.getImage());
        profileImageRepository.deleteById(imageId);
    }

    private void modifyProfileImage(UserEntity userEntity, ProfileImageRequestDto profileImageRequestDto) throws IOException {
        ProfileImageEntity profileImageEntity = findImageById(profileImageRequestDto.getId());
        s3Uploader.delete(profileImageEntity.getImage());
        String fileName = s3Uploader.upload(profileImageRequestDto.getImage(), "profileImage/" + userEntity.getUsername());
        profileImageEntity.setImage(fileName);
        profileImageRepository.save(profileImageEntity);
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }

    private ProfileImageEntity findImageById(Long imageId) {
        return profileImageRepository.findById(imageId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }
}
