package com.spring.reserve.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
@Builder
public class ReserveTimeDTO {
    private Long id;
    private String reserveId;
    private String timeId;
    private String reserveDate;
    private TimeDto time;

}
