package com.willyoubackend.domain.report.service;

import com.willyoubackend.domain.report.dto.ReportResponseDto;
import com.willyoubackend.domain.report.entity.Report;
import com.willyoubackend.domain.report.repository.ReportRepository;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.entity.UserRoleEnum;
import com.willyoubackend.domain.user.repository.UserRepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportSerivce {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ApiResponse<ReportResponseDto>> createReport(UserEntity reporter, UserEntity reported, String content) {
        Report report = new Report();
        report.setReporter(reporter);
        report.setReported(reported);
        report.setContent(content);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new ReportResponseDto(reportRepository.save(report))));
    }

    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportList(UserDetailsImpl user) {
        UserEntity currentUserEntity = user.getUser();

        if (!AuthorizationUtils.isAdmin(currentUserEntity)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }

        List<ReportResponseDto> reportResponseDtoList = reportRepository.findAll()
                .stream()
                .map(ReportResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(reportResponseDtoList));
    }
}
