package com.willyoubackend.domain.dating.repository.qdsl;

import com.willyoubackend.domain.dating.entity.ActivitiesDating;
import com.willyoubackend.domain.dating.entity.Activity;
import com.willyoubackend.domain.dating.entity.Dating;

import java.util.List;

public interface ActivitiesDatingCustomRepository {
    List<ActivitiesDating> findAllActivitiesDatingByDating(Dating dating);

    ActivitiesDating findByActivity(Activity activity);
}
