package com.willyoubackend.domain.user_profile.entity;

import com.willyoubackend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public ProfileImageEntity(String image){
        this.image = image;
    }

    public void setUserEntity(UserEntity userEntity){
        this.userEntity = userEntity;
    }
}
