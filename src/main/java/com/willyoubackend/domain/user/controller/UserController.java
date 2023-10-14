package com.willyoubackend.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.willyoubackend.domain.user.dto.*;
import com.willyoubackend.domain.user.entity.UserRoleEnum;
import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.security.UserDetailsImpl;
import com.willyoubackend.domain.user.service.KakaoService;
import com.willyoubackend.domain.user.service.UserService;
import com.willyoubackend.global.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
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
    private final JwtUtil jwtUtil;
    // 회원 가입

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        return userService.signup(signupRequestDto, bindingResult);
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

    @PostMapping("/email")
    public ResponseEntity<Void> authEmail(@RequestBody @Valid EmailRequest request) {
        userService.authEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ApiResponse<String> verifyEmail(@RequestBody @Valid VerifiRequest request) {
        return userService.verifyEmail(request);
    }

    // 엑세스 토큰 갱신
    @GetMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponseDto>> refreshAccessToken(@RequestParam String refreshToken, HttpServletResponse response) throws JsonProcessingException {
        if (StringUtils.hasText(refreshToken)) {
            // 리프레시 토큰을 사용하여 새로운 엑세스 토큰 생성
            String newAccessToken = refreshAccessTokenWithRefreshToken(refreshToken, response);
            if (newAccessToken != null) {
                return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new TokenResponseDto(newAccessToken)));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Refresh token is invalid or missing."));
    }

    private String refreshAccessTokenWithRefreshToken(String refreshToken, HttpServletResponse response) {
        if (jwtUtil.validateToken(refreshToken)) {
            Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
            String username = info.getSubject();
            UserRoleEnum role = UserRoleEnum.valueOf(String.valueOf(info.get("auth")));
            String newAccessToken = jwtUtil.createToken(username, role);

            // 엑세스 토큰을 응답 헤더에 추가
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
            newAccessToken = jwtUtil.substringToken(newAccessToken);

            return newAccessToken;
        }
        return null;
    }
}