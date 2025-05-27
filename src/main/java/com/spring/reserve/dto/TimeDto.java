package com.spring.reserve.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeDto {
    private Long id;
    private String time;
    private int reserved;
    private String reserveUserId;
}
