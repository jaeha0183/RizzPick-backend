package com.willyoubackend.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.user.entity.QUserEntity;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_profile.entity.GenderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUserEntity user = QUserEntity.userEntity;

    @Override
    public List<UserEntity> findByUserProfileEntity_LocationAndIdNot(String location, Long id) {
        return jpaQueryFactory.selectFrom(user)
                .leftJoin(user.profileImages).fetchJoin()
                .leftJoin(user.userProfileEntity).fetchJoin()
                .leftJoin(user.userProfileEntity.dating).fetchJoin()
                .where(
                        idNe(id))
                .fetch();
    }

    @Override
    public List<UserEntity> findByUserProfileEntity_LocationAndUserProfileEntity_GenderNotAndIdNot(String location, GenderEnum gender, Long id) {
        return jpaQueryFactory.selectFrom(user)
                .leftJoin(user.profileImages).fetchJoin()
                .leftJoin(user.userProfileEntity).fetchJoin()
                .leftJoin(user.userProfileEntity.dating).fetchJoin()
                .where(
                        genderNe(gender)
                                .and(idNe(id)))
                .fetch();
    }

    private BooleanExpression locationEq(String location) {
        return location != null ? user.userProfileEntity.location.eq(location) : null;
    }

    private BooleanExpression genderNe(GenderEnum gender) {
        return gender != null ? user.userProfileEntity.gender.ne(gender) : null;
    }

    private BooleanExpression idNe(Long id) {
        return id != null ? user.id.ne(id) : null;
    }
}