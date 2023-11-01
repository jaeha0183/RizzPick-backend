package com.willyoubackend.domain.report.dto;

import com.willyoubackend.domain.report.entity.Report;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportResponseDto {
    private final Long id;
    private final String content;

    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.content = report.getContent();
    }
}