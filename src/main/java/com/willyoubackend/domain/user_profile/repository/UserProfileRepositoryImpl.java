package com.willyoubackend.domain.user_profile.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.QUserProfileEntity;
import com.willyoubackend.domain.user_profile.entity.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements UserProfileRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUserProfileEntity userProfile = QUserProfileEntity.userProfileEntity;


    @Override
    public UserProfileEntity findByUserEntity(UserEntity user) {
        return jpaQueryFactory.select(userProfile)
                .from(userProfile)
                .where(userEntityEq(user))
                .fetchOne();
    }

    private BooleanExpression userEntityEq(UserEntity user) {
        return user != null ? userProfile.userEntity.eq(user) : null;
    }
}