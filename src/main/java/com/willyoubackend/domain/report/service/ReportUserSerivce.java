package com.willyoubackend.domain.report.service;

import com.willyoubackend.domain.report.dto.ReportUserResponseDto;
import com.willyoubackend.domain.report.entity.ReportUser;
import com.willyoubackend.domain.report.repository.ReportUserRepository;
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
public class ReportUserSerivce {
    private final ReportUserRepository reportUserRepository;

    public ResponseEntity<ApiResponse<ReportUserResponseDto>> createReportUser(UserEntity reporter, UserEntity reported, String content) {
        ReportUser reportUser = new ReportUser();
        reportUser.setReporter(reporter);
        reportUser.setReported(reported);
        reportUser.setContent(content);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new ReportUserResponseDto(reportUserRepository.save(reportUser))));
    }

    public ResponseEntity<ApiResponse<List<ReportUserResponseDto>>> getReportUserList(UserDetailsImpl user) {
        UserEntity currentUserEntity = user.getUser();

        if (!AuthorizationUtils.isAdmin(currentUserEntity)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }

        List<ReportUserResponseDto> reportUserResponseDtoList = reportUserRepository.findAll()
                .stream()
                .map(ReportUserResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(reportUserResponseDtoList));
    }
}