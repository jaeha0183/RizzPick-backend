package com.willyoubackend.domain.user_profile.repository;

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

    @Override
    public List<ProfileImageEntity> findAllByUserEntity(UserEntity userEntity) {

        QProfileImageEntity profileImageEntity = QProfileImageEntity.profileImageEntity;

        return jpaQueryFactory.selectFrom(profileImageEntity)
                .where(profileImageEntity.userEntity.eq(userEntity))
                .fetch();
    }
}