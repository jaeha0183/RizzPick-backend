package com.willyoubackend.domain.dating.entity;

import com.willyoubackend.domain.dating.dto.ActivityRequestDto;
import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Activity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "delete_status", nullable = false)
    private Boolean deleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public Activity(ActivityRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.deleteStatus = false;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void update(ActivityRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void setDeleteStatus(Boolean status) {
        this.deleteStatus = status;
    }
}