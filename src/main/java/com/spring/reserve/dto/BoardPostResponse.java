package com.spring.reserve.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardPostResponse {
    private String resultCode;
    private String resultMessage;
    private Long id;
}
