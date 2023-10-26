package com.willyoubackend.domain.user_profile.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.ProfileImageEntity;
import com.willyoubackend.domain.user_profile.entity.QProfileImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileImageRepositoryImpl implements ProfileImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProfileImageEntity profileImage = QProfileImageEntity.profileImageEntity;

    @Override
    public List<ProfileImageEntity> findAllByUserEntity(UserEntity user) {
        return jpaQueryFactory.selectFrom(profileImage)
                .where(userEntityEq(user))
                .fetch();
    }

    private BooleanExpression userEntityEq(UserEntity user) {
        return user != null ? profileImage.userEntity.eq(user) : null;
    }
}