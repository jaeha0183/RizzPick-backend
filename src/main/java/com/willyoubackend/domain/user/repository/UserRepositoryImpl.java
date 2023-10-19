package com.willyoubackend.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.user.entity.QUserEntity;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.GenderEnum;
import com.willyoubackend.domain.user_profile.entity.LocationEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserEntity> findByUserProfileEntity_LocationAndIdNot(LocationEnum location, Long id) {
        QUserEntity user = QUserEntity.userEntity;
        return jpaQueryFactory.selectFrom(user)
                .leftJoin(user.profileImages).fetchJoin()
                .leftJoin(user.userProfileEntity).fetchJoin()
                .leftJoin(user.userProfileEntity.dating).fetchJoin()
                .where(user.userProfileEntity.location.eq(location)
                        .and(user.id.ne(id)))
                .fetch();
    }

    @Override
    public List<UserEntity> findByUserProfileEntity_LocationAndUserProfileEntity_GenderNotAndIdNot(LocationEnum location, GenderEnum gender, Long id) {
        QUserEntity user = QUserEntity.userEntity;
        return jpaQueryFactory.selectFrom(user)
                .leftJoin(user.profileImages).fetchJoin()
                .leftJoin(user.userProfileEntity).fetchJoin()
                .leftJoin(user.userProfileEntity.dating).fetchJoin()
                .where(user.userProfileEntity.location.eq(location)
                        .and(user.userProfileEntity.gender.ne(gender))
                        .and(user.id.ne(id)))
                .fetch();
    }
}