package com.spring.reserve.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleUserDTO {
    private Long id;
    private String roleId;
    private String userId;
    private String roleName;
    private String roleDesc;
    private String reserveDate;
}
