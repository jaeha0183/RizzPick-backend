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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<ApiResponse<String>> authEmail(@RequestBody @Valid EmailRequest request) {
        userService.authEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("인증번호가 전송되었습니다."));
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

    @PostMapping("/verify-password")
    public ResponseEntity<ApiResponse<String>> verifyPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PasswordRequestDto requestDto) {
        userService.verifyPassword(userDetails.getUsername(), requestDto);
        return ResponseEntity.ok(ApiResponse.successMessage("비밀번호 인증 성공"));
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto,
                                             BindingResult bindingResult,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        if(bindingResult.hasErrors()) {

            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ApiResponse.error(errorMessage);
        }

        return userService.resetPassword(userDetails.getUsername(), requestDto);
    }

    @GetMapping("/is-new")
    public ResponseEntity<ApiResponse<Boolean>> checkIsNewStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isNew = userService.checkIsNewByUsername(username);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(isNew));
    }
    @PostMapping("/send-username")
    public ResponseEntity<ApiResponse<String>> sendUsernameToEmail(@RequestBody EmailRequest emailRequest) {
        userService.sendUsernameByEmail(emailRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successMessage("사용자의 username을 이메일로 전송하였습니다."));
    }

    @PostMapping("/reset-password-by-email")
    public ResponseEntity<ApiResponse<String>> resetPasswordByEmail(@RequestBody ResetPasswordByEmailRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.resetPasswordByEmail(requestDto));
    }

    @GetMapping("/validate-username")
    public ResponseEntity<ApiResponse<Boolean>> isUsernameExists(@RequestBody UsernameRequestDto requestDto) {
        boolean exists = userService.isUsernameExists(requestDto.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(exists));
    }
}