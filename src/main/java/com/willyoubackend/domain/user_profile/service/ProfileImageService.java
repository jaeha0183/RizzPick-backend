package com.willyoubackend.domain.user_profile.service;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.dto.ProfileImageRequestDto;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.repository.ProfileImageRepository;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import com.willyoubackend.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final S3Uploader s3Uploader;
    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;


    public void updateProfileImage(UserEntity userEntity, ProfileImageRequestDto profileImageRequestDto, List<MultipartFile> images) throws IOException {

        if (images.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_IMAGE);
        }

        UserEntity loggedInUser = findUserById(userEntity.getId());

        for (MultipartFile image : images) {

            String fileName = s3Uploader.upload(image, "profileImage/" + userEntity.getUsername());
            ProfileImageEntity profileImageEntity = new ProfileImageEntity(fileName);

            profileImageEntity.setUserEntity(loggedInUser);
            loggedInUser.getProfileImages().add(profileImageEntity);

            profileImageRepository.save(profileImageEntity);
        }
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }
}
