package com.example.dickrunner.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Rout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Lob
    private String coordinates;  // Храним координаты маршрута в виде JSON строки
}
