package com.willyoubackend.domain.dating.dto;

import com.willyoubackend.domain.dating.entity.Activity;
import lombok.Getter;

@Getter
public class ActivityResponseDto {
    private final Long activityId;
    private final Long userId;
    private final String activityContent;
    private final String location;

    public ActivityResponseDto(Activity activity) {
        this.activityId = activity.getId();
        this.userId = activity.getUser().getId();
        this.activityContent = activity.getContent();
        this.location = activity.getLocation();
    }
}