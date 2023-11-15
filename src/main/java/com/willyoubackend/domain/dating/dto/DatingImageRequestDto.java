package com.willyoubackend.domain.dating.dto;

import com.willyoubackend.domain.user_profile.dto.ImageActionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class DatingImageRequestDto {
    @Schema(description = "이미지 ID")
    private Long id;
    @Schema(description = "추가/수정/삭제", allowableValues = {"ADD", "DELETE", "MODIFY"})
    private ImageActionEnum action;
    @Schema(description = "이미지 URL")
    private MultipartFile image;
}
