package com.willyoubackend.domain.dating.repository.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.willyoubackend.domain.dating.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivitiesDatingCustomRepositoryImpl implements ActivitiesDatingCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    private final QActivitiesDating qActivitiesDating = QActivitiesDating.activitiesDating;
    @Override
    public List<ActivitiesDating> findAllActivitiesDatingByDating(Dating dating) {
        return jpaQueryFactory.selectFrom(qActivitiesDating)
                .where(qActivitiesDating.deleteStatus.eq(false), qActivitiesDating.dating.eq(dating))
                .fetch();
    }

    @Override
    public ActivitiesDating findByActivity(Activity activity) {
        return jpaQueryFactory.select(qActivitiesDating)
                .where(qActivitiesDating.activity.eq(activity))
                .fetchOne();
    }
}
