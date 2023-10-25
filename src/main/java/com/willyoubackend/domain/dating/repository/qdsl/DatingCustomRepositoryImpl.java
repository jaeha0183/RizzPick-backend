package com.willyoubackend.domain.dating.repository.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.dating.entity.QDating;
import com.willyoubackend.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DatingCustomRepositoryImpl implements DatingCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QDating qDating = QDating.dating;

    @Override
    public List<Dating> findAllByOrderByCreatedAt() {
        return jpaQueryFactory.selectFrom(qDating)
                .where(qDating.deleteStatus.eq(false))
                .fetch();
    }

    @Override
    public List<Dating> findAllByUserOrderByCreatedAt(UserEntity user) {
        return jpaQueryFactory.selectFrom(qDating)
                .where(
                        qDating.deleteStatus.eq(false),
                        qDating.user.eq(user)
                )
                .fetch();
    }

    @Override
    public List<Dating> findAllByLocationOrderByCreatedAt(String location) {
        return jpaQueryFactory.selectFrom(qDating)
                .where(
                        qDating.deleteStatus.eq(false),
                        qDating.location.eq(location)
                )
                .fetch();
    }

    @Override
    public List<Dating> findAllByUser(UserEntity user) {
        return jpaQueryFactory.selectFrom(qDating)
                .where(
                        qDating.deleteStatus.eq(false),
                        qDating.user.eq(user)
                )
                .fetch();

    }

}
