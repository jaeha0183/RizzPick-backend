package com.willyoubackend.domain.report.dto;

import com.willyoubackend.domain.report.entity.ReportUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportUserResponseDto {
    private final Long id;
    private final String content;
    private final String reporter; // 추가: 신고한 사람
    private final String reported; // 추가: 신고 받은 사람

    public ReportUserResponseDto(ReportUser reportUser) {
        this.id = reportUser.getId();
        this.reporter = reportUser.getReporter().getUsername(); // 추가
        this.reported = reportUser.getReported().getUsername(); // 추가
        this.content = reportUser.getContent();
    }
}