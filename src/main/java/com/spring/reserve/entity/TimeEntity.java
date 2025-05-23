package com.spring.reserve.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// DB의 테이블 역할을 하는 클래스
@Entity
@Getter
/*@Setter*/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "time")
public class TimeEntity{
  @Id // pk 컬럼 지정. 필수
  @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
  private Long id;

  private String time;

  private int reserved;

  private String reserveUserId;

  @OneToMany(mappedBy = "timeEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<ReserveTimeEntity> reserveTimeEntity = new ArrayList<>();


}
