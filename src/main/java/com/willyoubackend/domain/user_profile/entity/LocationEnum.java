package com.willyoubackend.domain.user_profile.entity;

import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.Getter;

@Deprecated
@Getter
public enum LocationEnum {
    NONE("없음"),
    SEOUL("서울"),
    BUSAN("부산"),
    INCHEON("인천"),
    DAEGU("대구"),
    DAEJEON("대전"),
    GWANGJU("광주"),
    ULSAN("울산");

    private final String themeName;

    LocationEnum(String themeName) {
        this.themeName = themeName;
    }

    public static LocationEnum findByThemeName(String themeName) {
        for (LocationEnum locationEnum : values()) {
            if (locationEnum.getThemeName().equals(themeName)) {
                return locationEnum;
            }
        }
        throw new CustomException(ErrorCode.INVALID_ENUM_VAL);
    }
}