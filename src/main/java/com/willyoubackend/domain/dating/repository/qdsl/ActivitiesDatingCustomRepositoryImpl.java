package com.willyoubackend.domain.dating.repository.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.dating.entity.ActivitiesDating;
import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.dating.entity.Dating;
import com.willyoubackend.domain.dating.entity.QActivitiesDating;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivitiesDatingCustomRepositoryImpl implements ActivitiesDatingCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QActivitiesDating qActivitiesDating = QActivitiesDating.activitiesDating;

    @Override
    public List<ActivitiesDating> findAllActivitiesDatingByDating(Dating dating) {
        if (dating == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qActivitiesDating)
                .where(qActivitiesDating.deleteStatus.eq(false), qActivitiesDating.dating.eq(dating))
                .fetch();
    }

    @Override
    public ActivitiesDating findByActivity(Activity activity) {
        if (activity == null) throw new CustomException(ErrorCode.NOT_FOUND_ENTITY);
        return jpaQueryFactory.selectFrom(qActivitiesDating)
                .where(qActivitiesDating.activity.eq(activity))
                .fetchOne();
    }
}
