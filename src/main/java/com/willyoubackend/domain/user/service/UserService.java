package com.willyoubackend.domain.user.service;

import com.willyoubackend.domain.user.dto.EmailRequest;
import com.willyoubackend.domain.user.dto.LoginResponseDto;
import com.willyoubackend.domain.user.dto.SignupRequestDto;
import com.willyoubackend.domain.user.dto.VerifiRequest;
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
import org.springframework.util.StringUtils;
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

    // 관리자 인증 토큰
    @Value("${admin.token}") // Base64 Encode 한 SecretKey
    private String ADMIN_TOKEN;

    // 회원가입
    public ResponseEntity<ApiResponse<String>> signup(SignupRequestDto requestDto, BindingResult bindingResult) {

        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ApiResponse.error(fieldError.getDefaultMessage()));
            }
        }

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        // 회원 중복 확인
        Optional<UserEntity> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        // 이메일 중복 확인
        Optional<UserEntity> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        //사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CustomException(ErrorCode.NOT_AUTHORIZED);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        UserEntity userEntity = new UserEntity(username, password, email, role);
        userRepository.save(userEntity);

        // userProfileEntity 생성
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setUserEntity(userEntity);
        userProfileRepository.save(userProfileEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successMessage("회원가입이 완료되었습니다."));
    }

    public ResponseEntity<ApiResponse<LoginResponseDto>> userStatusCheck(UserEntity user) {
        UserProfileEntity userProfileEntity = userProfileRepository.findByUserEntity(user);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new LoginResponseDto(user.getId(), userProfileEntity.isUserActiveStatus())));
    }

    // 회원정보 수정
    @Transactional
    public void update(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_ENTITY));
    }


    @Transactional
    public void authEmail(EmailRequest request) {
// 임의의 authKey 생성
        try {
            if (redisUtil.getData(request.getEmail()) != null) {
                throw new CustomException(ErrorCode.DUPLICATE_VERIFI_CODE);
            }
        } catch (NullPointerException e) {
            log.info("email : " + request.getEmail());
        }
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);// 범위 : 111111 ~ 999999
        log.info("authKey : " + authKey);

// 이메일 발송
        sendAuthEmail(request.getEmail(), authKey);
        log.info("email : " + request.getEmail());
        log.info("status : " + HttpStatus.OK);
    }

    private void sendAuthEmail(String email, String authKey) {

        String subject = "Will You 회원가입 인증 메일입니다.";
        String text = "인증번호는 " + authKey + "입니다. <br/>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true); //포함된 텍스트가 HTML이라는 의미로 true.
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        redisUtil.setDataExpire(email, authKey, 3 * 60 * 1L);
    }

    // 이메일 인증
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
        if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
            Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
            String username = info.getSubject();
            UserRoleEnum role = UserRoleEnum.valueOf(String.valueOf(info.get("auth")));
            String newAccessToken = jwtUtil.createToken(username, role);
            newAccessToken = jwtUtil.substringToken(newAccessToken);
            return newAccessToken;
        }
        return null;
    }

    //Id 로 userName 추축
    public String getUserNameById(Long id){
        return userRepository.findById(id).get().getUsername();
    }
}