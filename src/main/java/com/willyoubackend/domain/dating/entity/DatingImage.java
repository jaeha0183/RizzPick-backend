package com.willyoubackend.domain.dating.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class DatingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String image;

    @OneToOne
    @JoinColumn(name="dating_id")
    private Dating dating;

    public DatingImage(String image) {
        this.image = image;
    }

    public void setDating(Dating dating) {
        this.dating = dating;
    }

    public void update(String image) {
        this.image = image;
    }
}
