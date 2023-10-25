package com.willyoubackend.domain.dating.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ActivitiesDating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delete_status", nullable = false)
    private Boolean deleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dating_id")
    private Dating dating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    public ActivitiesDating(Dating dating, Activity activity) {
        this.dating = dating;
        this.activity = activity;
        this.deleteStatus = false;
    }

    public void setDeleteStatus(Boolean status) {
        this.deleteStatus = status;
    }
}