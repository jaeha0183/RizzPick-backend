package com.willyoubackend.domain.report.dto;

import com.willyoubackend.domain.report.entity.ReportUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportUserResponseDto {
    private final Long id;
    private final String content;

    public ReportUserResponseDto(ReportUser reportUser) {
        this.id = reportUser.getId();
        this.content = reportUser.getContent();
    }
}