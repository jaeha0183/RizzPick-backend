package com.willyoubackend.domain.user_profile.entity;

import lombok.Getter;

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

    LocationEnum(String themeName){
        this.themeName = themeName;
    }
}