package com.willyoubackend.domain.user.service;

import com.willyoubackend.domain.user.dto.*;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.entity.UserRoleEnum;
import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.repository.RefreshTokenRepository;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.repository.UserProfileRepository;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import com.willyoubackend.global.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final UserProfileRepository userProfileRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${admin.token}") // Base64 Encode 한 SecretKey
    private String ADMIN_TOKEN;

    public ResponseEntity<ApiResponse<String>> signup(SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ApiResponse.error(fieldError.getDefaultMessage()));
            }
        }

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        Optional<UserEntity> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        Optional<UserEntity> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CustomException(ErrorCode.NOT_AUTHORIZED);
            }
            role = UserRoleEnum.ADMIN;
        }

        UserEntity userEntity = new UserEntity(username, password, email, role);
        userRepository.save(userEntity);
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setUserEntity(userEntity);
        userProfileRepository.save(userProfileEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successMessage("회원가입이 완료되었습니다."));
    }

    public ResponseEntity<ApiResponse<LoginResponseDto>> userStatusCheck(UserEntity user) {
        UserProfileEntity userProfileEntity = userProfileRepository.findByUserEntity(user);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new LoginResponseDto(user.getId(), userProfileEntity.isUserActiveStatus())));
    }

    @Transactional
    public void authEmail(EmailRequest request) {
        String email = request.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        try {
            if (redisUtil.getData(request.getEmail()) != null) {
                throw new CustomException(ErrorCode.DUPLICATE_VERIFI_CODE);
            }
        } catch (NullPointerException e) {
        }
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);
        sendAuthEmail(request.getEmail(), authKey);
    }

    private void sendAuthEmail(String email, String authKey) {
        String subject = "Will You 회원가입 인증 메일입니다.";
        String text = "인증번호는 " + authKey + "입니다. <br/>";
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        redisUtil.setDataExpire(email, authKey, 3 * 60 * 1L);
    }

    public ApiResponse<String> verifyEmail(VerifiRequest request) {
        String email = request.getEmail();
        String authKey = request.getAuthKey();

        String redisAuthKey = redisUtil.getData(email);
        if (redisAuthKey == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_VERIFI_CODE);
        }

        if (!redisAuthKey.equals(authKey)) {
            throw new CustomException(ErrorCode.WRONG_VERIFI_CODE);
        }

        redisUtil.deleteData(email);
        return ApiResponse.successMessage("인증이 완료되었습니다.");
    }

    public String refreshAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            log.info("Token이 만료 되었습니다.");
            throw new JwtException("Refresh Token Error");
        }
        if (!refreshTokenRepository.existsByToken(refreshToken)) {
            log.info("Token이 만료 되었습니다.");
            throw new JwtException("Refresh Token Error");
        }

        Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
        String username = info.getSubject();
        UserRoleEnum role = UserRoleEnum.valueOf(String.valueOf(info.get("auth")));
        String newAccessToken = jwtUtil.createToken(username, role);
        newAccessToken = jwtUtil.substringToken(newAccessToken);
        return newAccessToken;
    }

    @Transactional
    public ApiResponse<String> resetPassword(String username, ResetPasswordRequestDto requestDto) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userEntity.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(userEntity);

        return ApiResponse.successMessage("비밀번호가 변경되었습니다.");
    }

    public void verifyPassword(String username, PasswordRequestDto requestDto) {
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean isMatched = passwordEncoder.matches(requestDto.getPassword(), currentUser.getPassword());

        if (!isMatched) {
            throw new CustomException(ErrorCode.PASSWORD_VERIFICATION_FAILED);
        }
    }

    public boolean checkIsNewByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserProfileEntity userProfileEntity = userProfileRepository.findByUserEntity(userEntity);

        return userProfileEntity.isNew();
    }

    @Transactional
    public void sendUsernameByEmail(EmailRequest request) {
        String email = request.getEmail();

        // DB에서 email을 기반으로 username 찾기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String username = user.getUsername();

        sendUsernameEmail(email, username);
    }

    private void sendUsernameEmail(String email, String username) {
        String subject = "Will You ID 조회 서비스입니다.";
        String text = "해당 이메일로 가입한 ID 는 " + username + " 입니다.";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public ApiResponse<String> resetPasswordByEmail(ResetPasswordByEmailRequestDto requestDto) {
        UserEntity userEntity = userRepository.findByUsernameAndEmail(requestDto.getUsername(), requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newPassword = generateRandomPassword();
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);

        sendResetPasswordEmail(requestDto.getEmail(), newPassword);

        return ApiResponse.successMessage("비밀번호가 초기화되었으며, 새로운 비밀번호가 이메일로 전송되었습니다.");
    }

    private String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        // 랜덤 알파벳 문자 추가 (대소문자 각각 적어도 한 개)
        password.append((char) (random.nextInt(26) + 'A'));
        password.append((char) (random.nextInt(26) + 'a'));

        // 랜덤 숫자 추가
        password.append(random.nextInt(10));

        // 랜덤 특수문자 추가
        String specialChars = "@#$%^&+=!";
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // 나머지 길이를 채울 때까지 랜덤 문자 추가
        String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=!";
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        return password.toString();
    }

    private void sendResetPasswordEmail(String email, String newPassword) {
        String subject = "Will You 비밀번호 초기화 서비스입니다.";
        String text = "새로운 비밀번호는 " + newPassword + " 입니다.";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


}