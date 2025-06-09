package com.spring.reserve.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveDTO {
    private Long id;
    private String reserveReason;
    private String reserveDate;
    private String reservePeriod;
    private String userName;
    private String reserveUserId;

    private Long hallId;
    private List<Long> reserveTimeSave;
    private List<ReserveTimeDTO> reserveTime;
    private LocalDateTime reserveCreatedTime;
    private LocalDateTime reserveUpdatedTime;

}
