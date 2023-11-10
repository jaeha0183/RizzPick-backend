package com.willyoubackend.domain.dating.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class DatingRequestDto {
    private String title;
    private String location;
    private String theme;
}