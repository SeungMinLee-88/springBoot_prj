package com.spring.reserve.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveTimeDTO {
    private Long id;
    private String reserveId;
    private String timeId;
    private String reserveDate;
    private TimeDto time;

}
