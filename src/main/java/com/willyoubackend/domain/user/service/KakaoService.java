package com.willyoubackend.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.willyoubackend.domain.user.dto.KakaoUserInfoDto;
import com.willyoubackend.domain.user.dto.LoginResponseDto;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.entity.UserRoleEnum;
import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.repository.UserProfileRepository;
import com.willyoubackend.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    @Value("${client.id}")
    private String CLIENT_ID;

    public ResponseEntity<ApiResponse<LoginResponseDto>> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String[] tokens = new String[]{Arrays.toString(getToken(code))};
        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(tokens[0], tokens[1]);

        // 3. 필요시에 회원가입
        UserEntity kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 반환
        String createToken = jwtUtil.createToken(kakaoUser.getUsername(), kakaoUser.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        // Kakao User active status
        UserProfileEntity userProfileEntity = userProfileRepository.findByUserEntity(kakaoUser);
        LoginResponseDto responseDto = new LoginResponseDto(
                kakaoUser.getId(),
                userProfileEntity.isUserActiveStatus()

        );
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(responseDto));
    }

    private String[] getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
//        log.info("인가코드:" + code);
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization");
        body.add("client_id", CLIENT_ID);
//        body.add("client_id", CLIENT_ID); 프론트랑 맞추고 코드 수정예정
        body.add("redirect_uri", "http://localhost:3000/login/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        // log.info(jsonNode.get("access_token").asText());
        String accessToken = jsonNode.get("access_token").asText();
        String refreshToken = jsonNode.get("refresh_token").asText();
        return new String[]{accessToken, refreshToken};
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken, String refreshToken) throws JsonProcessingException {
//        log.info("accessToken:" + accessToken);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", "Bearer " + accessToken);
        headers.add("refreshToken", "Bearer " + refreshToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        // Wooyong Jeong
        int indexAt = 0;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                indexAt = i;
            }
        }
        return new KakaoUserInfoDto(id, email.substring(0, indexAt), email);
    }

    private UserEntity registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        UserEntity kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            UserEntity sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new UserEntity(
                        encodedPassword,
                        email,
                        UserRoleEnum.USER,
                        kakaoId
                );
            }
            UserEntity savedUserEntity = userRepository.save(kakaoUser);
            // Wooyong jeong - User Profile Create with dummy value
            UserProfileEntity userProfileEntity = new UserProfileEntity();
            userProfileEntity.setUserEntity(savedUserEntity);
            userProfileRepository.save(userProfileEntity);
        }
        return kakaoUser;
    }
}