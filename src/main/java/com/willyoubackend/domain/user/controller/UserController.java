package com.willyoubackend.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.willyoubackend.domain.user.dto.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        return userService.signup(signupRequestDto, bindingResult);
    }

    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        return new UserInfoDto(username);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<LoginResponseDto>> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);
    }

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

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponseDto>> refreshAccessToken(@RequestBody TokenRequestDto refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        String newAccessToken = userService.refreshAccessToken(refreshToken);

        if (newAccessToken != null) {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new TokenResponseDto(newAccessToken)));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Refresh token is invalid or missing."));
        }
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto,
                                             BindingResult bindingResult,
                                             @RequestHeader("Authorization") String token) {
        if(bindingResult.hasErrors()) {
            // 에러 메시지를 응답으로 전달
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ApiResponse.error(errorMessage);
        }

        String username = jwtUtil.getUsernameFromToken(token); // 예제에서는 JWT를 사용
        return userService.resetPassword(username, requestDto);
    }
}