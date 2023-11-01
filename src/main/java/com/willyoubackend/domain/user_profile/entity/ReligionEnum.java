package com.willyoubackend.domain.user_profile.entity;

import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ReligionEnum {
    NONE("없음"),
    IRRELIGION("무교"),
    CHRISTIANITY("기독교"),
    JUDAISM("유대교"),
    ISLAM("이슬람교"),
    CATHOLICISM("가톨릭교"),
    HINDUISM("힌두교"),
    BUDDHISM("불교"),
    CONFUCIANISM("유교"),
    OTHERS("기타");

    private final String themeName;

    ReligionEnum(String themeName) {
        this.themeName = themeName;
    }

    public static ReligionEnum findByThemeName(String themeName) {
        for (ReligionEnum religionEnum : values()) {
            if (religionEnum.getThemeName().equals(themeName))
                return religionEnum;
        }
        throw new CustomException(ErrorCode.INVALID_ENUM_VAL);
    }
}