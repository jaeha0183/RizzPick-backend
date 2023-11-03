package com.willyoubackend.domain.user_like_match.repository.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.domain.user_like_match.entity.QUserMatchStatus;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserMatchStatusCustomRepositoryImpl implements UserMatchStatusCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    private final QUserMatchStatus qUserMatchStatus = QUserMatchStatus.userMatchStatus;
    @Override
    public Boolean existByUserOneAndUserTwo(UserEntity sentUser, UserEntity receivedUser) {
        if (sentUser == null || receivedUser == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qUserMatchStatus)
                .where(
                        qUserMatchStatus.userMatchedOne.eq(receivedUser),
                        qUserMatchStatus.userMatchedTwo.eq(sentUser)
                )
                .fetchFirst() != null;
    }

    @Override
    public Boolean existByUserTwoAndUserOne(UserEntity sentUser, UserEntity receivedUser) {
        if (sentUser == null || receivedUser == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qUserMatchStatus)
                .where(
                        qUserMatchStatus.userMatchedOne.eq(sentUser),
                        qUserMatchStatus.userMatchedTwo.eq(receivedUser)
                )
                .fetchFirst() != null;
    }
}
