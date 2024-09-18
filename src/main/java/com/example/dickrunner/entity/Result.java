package com.example.dickrunner.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double distance;

    @ManyToOne
    @JoinColumn(name = "rout_id")
    private Rout rout;

}
