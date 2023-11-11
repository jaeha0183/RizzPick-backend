package com.willyoubackend.domain.dating.dto;

import com.willyoubackend.domain.user_profile.dto.ImageActionEnum;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class DatingImageRequestDto {
    private Long id;
    private ImageActionEnum action;
    private MultipartFile image;
}
