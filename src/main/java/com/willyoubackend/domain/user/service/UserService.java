package com.willyoubackend.domain.user.service;

import com.willyoubackend.domain.user.dto.SignupRequestDto;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.entity.UserRoleEnum;
import com.willyoubackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.willyoubackend.domain.user.entity.UserRoleEnum.ADMIN;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 관리자 인증 토큰
    @Value("${admin.token}") // Base64 Encode 한 SecretKey
    private String ADMIN_TOKEN;

    // 회원가입
    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<UserEntity> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<UserEntity> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email이 존재합니다.");
        }

        //사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 유효하지 않아 등록이 불가능합니다.");
            }
            role = ADMIN;
        }

        // 사용자 등록
        UserEntity userEntity = UserEntity.builder().username(username).password(password).email(email).role(role).build();
        userRepository.save(userEntity);
    }

    // 회원정보 조회
    @Transactional(readOnly = true)
    public UserEntity getProfile(String username) {
        // 유효성 검사
        if (username == null) {
            throw new IllegalArgumentException("사용자 이름을 입력해 주세요.");
        }

        // 회원 정보 조회
        return findByUsername(username);
    }

    // 회원정보 수정
    @Transactional
    public void update(UserEntity userEntity) {
        userRepository.save(userEntity);
    }


    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자 이름의 회원을 찾을 수 없습니다. 사용자 이름: " + username));
    }
}