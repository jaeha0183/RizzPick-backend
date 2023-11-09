package com.willyoubackend.domain.report.dto;

import com.willyoubackend.domain.report.entity.ReportDating;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportDatingResponseDto {
    private final Long id;
    private final String content;
    private final String reporter; // 추가: 신고한 사람
    private final String reported; // 추가: 신고 받은 사람

    public ReportDatingResponseDto(ReportDating reportDating) {
        this.id = reportDating.getId();
        this.content = reportDating.getContent();
        this.reporter = reportDating.getReporter().getUsername(); // 추가
        this.reported = reportDating.getReported().getUsername(); // 추가
    }
}