package com.willyoubackend.domain.user_like_match.repository.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.QUserLikeStatus;
import com.willyoubackend.domain.user_like_match.entity.UserLikeStatus;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserLikeStatusCustomRepositoryImpl implements UserLikeStatusCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QUserLikeStatus qUserLikeStatus = QUserLikeStatus.userLikeStatus;

    @Override
    public UserLikeStatus findBySentUserAndReceivedUser(UserEntity sentUser, UserEntity receivedUser) {
        if (sentUser == null || receivedUser == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qUserLikeStatus)
                .where(
                        qUserLikeStatus.receivedUser.eq(receivedUser),
                        qUserLikeStatus.sentUser.eq(sentUser)
                )
                .fetchOne();
    }

    @Override
    public List<UserLikeStatus> findAllBySentUser(UserEntity sentUser) {
        if (sentUser == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qUserLikeStatus)
                .where(
                        qUserLikeStatus.sentUser.eq(sentUser)
                )
                .fetch();
    }

    @Override
    public List<UserLikeStatus> findAllByReceivedUser(UserEntity receivedUser) {
        if (receivedUser == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qUserLikeStatus)
                .where(
                        qUserLikeStatus.receivedUser.eq(receivedUser)
                )
                .fetch();
    }

    @Override
    public Boolean existsByReceivedUser(UserEntity user) {
        if (user == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qUserLikeStatus)
                .where(
                        qUserLikeStatus.receivedUser.eq(user)
                )
                .fetchFirst() != null;
    }
}
