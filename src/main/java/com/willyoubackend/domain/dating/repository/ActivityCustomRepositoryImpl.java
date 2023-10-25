package com.willyoubackend.domain.dating.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.dating.entity.QActivity;
import com.willyoubackend.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityCustomRepositoryImpl implements ActivityCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QActivity qActivity = QActivity.activity;

    @Override
    public List<Activity> findAllByOrderByCreatedAt() {
        return jpaQueryFactory.selectFrom(qActivity)
                .where(qActivity.deleteStatus.eq(false))
                .fetch();
    }

    @Override
    public List<Activity> findAllByUserOrderByCreatedAtDesc(UserEntity user) {
        return jpaQueryFactory.selectFrom(qActivity)
                .where(
                        qActivity.deleteStatus.eq(false),
                        qActivity.user.eq(user)
                )
                .fetch();
    }
}
