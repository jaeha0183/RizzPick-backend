package com.willyoubackend.domain.report.entity;

import com.willyoubackend.domain.user.entity.UserEntity;
import com.willyoubackend.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Report extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private UserEntity reporter;

    @ManyToOne
    @JoinColumn(name = "reported_id", nullable = false)
    private UserEntity reported;

    @Column(nullable = false)
    private String content;
}
