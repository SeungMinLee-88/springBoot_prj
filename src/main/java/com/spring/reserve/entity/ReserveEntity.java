package com.spring.reserve.entity;

import com.spring.reserve.dto.ReserveDTO;
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
@Table(name = "reserve")
public class ReserveEntity extends BaseEntity {
  @Id // pk 컬럼 지정. 필수
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String reserveReason;
  private String reserveDate;
  private String reservePeriod;

  private String reserveUserId;
  private String userName;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hall_id")
  private HallEntity hallEntity;


  @OneToMany(mappedBy = "reserveEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<ReserveTimeEntity> reserveTimeEntity = new ArrayList<>();



  @Data
  public class SearchCriteria {

    private String searchKey;
    private String searchValue;
  }


  public static ReserveEntity toSaveEntity(ReserveDTO reserveDTO, UserEntity userEntity, HallEntity hallEntity) {
    return ReserveEntity.builder()
            .id(reserveDTO.getId())
            .reserveReason(reserveDTO.getReserveReason())
            .reserveDate(reserveDTO.getReserveDate())
            .reservePeriod(reserveDTO.getReservePeriod())
            .userEntity(userEntity)
            .reserveUserId(reserveDTO.getReserveUserId())
            .userName(reserveDTO.getUserName())
            .hallEntity(hallEntity)
            .build();
  }

}
