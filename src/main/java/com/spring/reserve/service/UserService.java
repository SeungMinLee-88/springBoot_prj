package com.spring.reserve.service;

import com.spring.reserve.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface UserService {

  void userUpdate(UserDto userDto);

  void joinUser(UserDto userDto);

  Page<UserDto> userList(Pageable pageable, Map<String, String> params);

  UserDto userDetail(UserDto userDto);

  List<RoleDTO> roleList(List<Long> roleIds);

  void userDelete(Long id);
}
