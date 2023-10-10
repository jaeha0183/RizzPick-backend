package com.willyoubackend.domain.user_profile.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class ProfileImageRequestDto {
    private Long id;
    private ImageActionEnum action;
    private MultipartFile image;
}
