package com.willyoubackend.domain.report.dto;

import com.willyoubackend.domain.report.entity.ReportDating;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportDatingResponseDto {
    private final Long id;
    private final String content;

    public ReportDatingResponseDto(ReportDating reportDating) {
        this.id = reportDating.getId();
        this.content = reportDating.getContent();
    }
}
