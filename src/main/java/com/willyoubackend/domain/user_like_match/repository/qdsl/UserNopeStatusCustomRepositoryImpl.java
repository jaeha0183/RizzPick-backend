package com.willyoubackend.domain.user_like_match.repository.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.QUserNopeStatus;
import com.willyoubackend.domain.user_like_match.entity.UserNopeStatus;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserNopeStatusCustomRepositoryImpl implements UserNopeStatusCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QUserNopeStatus qUserNopeStatus = QUserNopeStatus.userNopeStatus;

    @Override
    public UserNopeStatus findBySentUserAndReceivedUser(UserEntity sentUser, UserEntity receivedUser) {
        if (sentUser == null || receivedUser == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qUserNopeStatus)
                .where(
                        qUserNopeStatus.receivedUser.eq(receivedUser).and(qUserNopeStatus.sentUser.eq(sentUser))
                )
                .fetchOne();
    }

    @Override
    public Boolean existBySentUserAndReceivedUser(UserEntity sentUser, UserEntity receivedUser) {
        if (sentUser == null || receivedUser == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qUserNopeStatus)
                .where(
                        qUserNopeStatus.receivedUser.eq(receivedUser).and(qUserNopeStatus.sentUser.eq(sentUser))
                )
                .fetchFirst() != null;
    }
}
