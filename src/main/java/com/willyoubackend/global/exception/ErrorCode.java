package com.willyoubackend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰 입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    WRONG_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),

    NOT_FOUND_ENTITY(HttpStatus.NOT_FOUND, "해당 데이터가 존재하지 않습니다."),

    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임을 입력해야 합니다."),
    INVALID_IMAGE(HttpStatus.BAD_REQUEST, "이미지를 하나 이상 등록하여야 합니다."),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "유효하지 않은 요청값입니다."),
    INVALID_ENUM_VAL(HttpStatus.BAD_REQUEST, "유효하지 않은 열거값입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
