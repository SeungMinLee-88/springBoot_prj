package com.spring.reserve.entity;

import com.spring.reserve.dto.ReserveDTO;
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
@Table(name = "reserve_time")
public class ReserveTimeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_id")
    private ReserveEntity reserveEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private TimeEntity timeEntity;

    private String reserveDate;

    public static ReserveTimeEntity toSaveEntity(ReserveEntity reserveEntity, TimeEntity timeEntity, ReserveDTO reserveDTO) {
        return ReserveTimeEntity.builder()
                .reserveEntity(reserveEntity)
                .timeEntity(timeEntity)
                .reserveDate(reserveDTO.getReserveDate())
                .build();
    }
}
