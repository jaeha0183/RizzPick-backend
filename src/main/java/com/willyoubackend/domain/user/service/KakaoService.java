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
        String[] tokens = getToken(code);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken, refreshToken);
        UserEntity kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
        String createToken = jwtUtil.createToken(kakaoUser.getUsername(), kakaoUser.getRole());
        String createRefresh = jwtUtil.createRefreshToken(kakaoUser.getUsername(), kakaoUser.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);
        response.addHeader(JwtUtil.REFRESH_HEADER, createRefresh);
        UserProfileEntity userProfileEntity = userProfileRepository.findByUserEntity(kakaoUser);
        LoginResponseDto responseDto = new LoginResponseDto(
                kakaoUser.getId(),
                userProfileEntity.isUserActiveStatus()
        );
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(responseDto));
    }

    private String[] getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", "https://rizzpick.com/login/kako/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        String accessToken = jsonNode.get("access_token").asText();
        String refreshToken = jsonNode.get("refresh_token").asText();
        return new String[]{accessToken, refreshToken};
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken, String refreshToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Authorization_Refresh", "Bearer " + refreshToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        int indexAt = 0;
        log.info(email);
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                indexAt = i;
            }
        }
        return new KakaoUserInfoDto(id, email.substring(0, indexAt), email);
    }

    private UserEntity registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        UserEntity kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            String kakaoEmail = kakaoUserInfo.getEmail();
            UserEntity sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);
                String email = kakaoUserInfo.getEmail();
                int indexAt = 0;
                log.info(email);
                for (int i = 0; i < email.length(); i++) {
                    if (email.charAt(i) == '@') {
                        indexAt = i;
                    }
                }
                kakaoUser = new UserEntity(
                        email.substring(0, indexAt),
                        encodedPassword,
                        email,
                        UserRoleEnum.USER,
                        kakaoId
                );
            }
            UserEntity savedUserEntity = userRepository.save(kakaoUser);
            UserProfileEntity userProfileEntity = new UserProfileEntity();
            userProfileEntity.setUserEntity(savedUserEntity);
            userProfileRepository.save(userProfileEntity);
        }
        return kakaoUser;
    }
}