package com.willyoubackend.domain.dating.repository.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.dating.entity.QDating;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.QUserLikeStatus;
import com.willyoubackend.domain.user_like_match.entity.QUserMatchStatus;
import com.willyoubackend.domain.user_like_match.entity.QUserNopeStatus;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DatingCustomRepositoryImpl implements DatingCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QDating qDating = QDating.dating;
    private final QUserNopeStatus qUserNopeStatus = QUserNopeStatus.userNopeStatus;
    private final QUserLikeStatus qUserLikeStatus = QUserLikeStatus.userLikeStatus;

    private final QUserMatchStatus qUserMatchStatus = QUserMatchStatus.userMatchStatus;

    @Override
    public List<Dating> findAllByOrderByCreatedAt(UserEntity user) {
        return jpaQueryFactory.selectFrom(qDating)
                .where(
                        qDating.deleteStatus.eq(false),
                        qDating.activeStatus.eq(true),
                        qDating.user.ne(user)
                )
                .orderBy(qDating.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Dating> findAllByUserOrderByCreatedAt(UserEntity user) {
        if (user == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
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
        if (user == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qDating)
                .where(
                        qDating.deleteStatus.eq(false),
                        qDating.user.eq(user)
                )
                .fetch();

    }

}
