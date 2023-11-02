package com.willyoubackend.global.util;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user.entity.UserRoleEnum;

public class AuthorizationUtils {
    public static boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRole() == UserRoleEnum.ADMIN;
    }
}

