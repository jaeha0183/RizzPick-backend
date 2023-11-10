package com.willyoubackend.domain.report.service;

import com.willyoubackend.domain.report.dto.ReportDatingResponseDto;
import com.willyoubackend.domain.report.entity.ReportDating;
import com.willyoubackend.domain.report.repository.ReportDatingRepository;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import com.willyoubackend.global.util.AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportDatingSerivce {
    private final ReportDatingRepository reportDatingRepository;

    public ResponseEntity<ApiResponse<ReportDatingResponseDto>> createReportDating(UserEntity reporter, UserEntity reported, String content) {
        ReportDating reportDating = new ReportDating();
        reportDating.setReporter(reporter);
        reportDating.setReported(reported);
        reportDating.setContent(content);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new ReportDatingResponseDto(reportDatingRepository.save(reportDating))));
    }

    public ResponseEntity<ApiResponse<List<ReportDatingResponseDto>>> getReportDatingList(UserDetailsImpl user) {
        UserEntity currentUserEntity = user.getUser();

        if (!AuthorizationUtils.isAdmin(currentUserEntity)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }

        List<ReportDatingResponseDto> reportDatingResponseDtoList = reportDatingRepository.findAll()
                .stream()
                .map(ReportDatingResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(reportDatingResponseDtoList));
    }
}