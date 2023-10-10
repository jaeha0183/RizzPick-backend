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

    }

    private void addProfileImage(UserEntity userEntity, ProfileImageRequestDto profileImageRequestDto) throws IOException {
        String fileName = s3Uploader.upload(profileImageRequestDto.getImage(), "profileImage/" + userEntity.getUsername());
        ProfileImageEntity profileImageEntity = new ProfileImageEntity(fileName);
        profileImageEntity.setUserEntity(userEntity);
        profileImageRepository.save(profileImageEntity);
    }

    private void deleteProfileImage(Long imageId) {
        ProfileImageEntity profileImageEntity = findImageById(imageId);
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
