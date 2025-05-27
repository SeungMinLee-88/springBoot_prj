package com.spring.reserve.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String loginId;
    private String userName;
    private String userPassword;
    private List<Long> roleUserSave;
    private List<RoleUserDTO> roleUser;

    public UserDto( Long id, String loginId, String userName, String userPassword,List<RoleUserDTO> roleUser) {
        this.id = id;
        this.loginId = loginId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.roleUser = roleUser;
    }
}
