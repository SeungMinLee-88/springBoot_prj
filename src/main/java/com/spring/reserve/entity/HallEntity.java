package com.spring.reserve.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
/*@Setter*/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hall")
public class HallEntity {
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    private String hallName;

    @OneToMany(mappedBy = "hallEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<ReserveEntity> reserveEntities = new ArrayList<>();
}
