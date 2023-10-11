package com.willyoubackend.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.willyoubackend.domain.user.dto.LoginResponseDto;
import com.willyoubackend.domain.user.dto.SignupRequestDto;
import com.willyoubackend.domain.user.dto.UserInfoDto;
import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user.service.KakaoService;
import com.willyoubackend.domain.user.service.UserService;
import com.willyoubackend.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;
    // 회원 가입

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        return userService.signup(signupRequestDto,bindingResult);
    }

    // 회원 관련 정보 받기
    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        return new UserInfoDto(username);
    }

    // 카카오 로그인
    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<LoginResponseDto>> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);
    }

    // 유저 활성화 항새 확인
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<LoginResponseDto>> userStatusCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.userStatusCheck(userDetails.getUser());
    }
}