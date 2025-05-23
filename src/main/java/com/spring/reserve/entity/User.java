package com.spring.reserve.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
/*@Setter*/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class User extends BaseEntity {
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(length = 20) // 크기 20, not null
    private String fistname;

    @Column // 크기 255, null 가능
    private String lastname;

}
