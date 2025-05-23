package com.spring.reserve.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
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
