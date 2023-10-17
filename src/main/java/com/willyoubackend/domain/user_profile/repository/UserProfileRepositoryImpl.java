package com.willyoubackend.domain.user_profile.repository;

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

    @Override
    public UserProfileEntity findByUserEntity(UserEntity userEntity) {

        QUserProfileEntity userProfileEntity = QUserProfileEntity.userProfileEntity;

        return jpaQueryFactory.select(userProfileEntity)
                .from(userProfileEntity)
                .where(userProfileEntity.userEntity.eq(userEntity))
                .fetchOne();
    }
}
