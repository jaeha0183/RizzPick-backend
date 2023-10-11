package com.willyoubackend.domain.user.service;

import com.willyoubackend.domain.user.dto.LoginResponseDto;
import com.willyoubackend.domain.user.dto.SignupRequestDto;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.entity.UserRoleEnum;
import com.willyoubackend.domain.user.repository.UserRepository;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import com.willyoubackend.domain.user_profile.repository.UserProfileRepository;
import com.willyoubackend.global.dto.ApiResponse;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

import static com.willyoubackend.domain.user.entity.UserRoleEnum.ADMIN;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository userProfileRepository;

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
}