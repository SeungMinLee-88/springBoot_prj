package com.spring.reserve.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
@Builder
public class RoleDTO {

    private Long id;
    private Long roleId;
    private String roleName;
    private String roleDesc;
}
