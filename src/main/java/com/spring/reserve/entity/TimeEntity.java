package com.spring.reserve.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "time")
public class TimeEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String time;

  private int reserved;

  private String reserveUserId;

  @OneToMany(mappedBy = "timeEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<ReserveTimeEntity> reserveTimeEntity = new ArrayList<>();


}
