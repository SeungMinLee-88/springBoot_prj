package com.spring.reserve.controller;

import com.spring.reserve.dto.RoleDTO;
import com.spring.reserve.dto.UserDto;
import com.spring.reserve.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@ResponseBody
public class RestUserController {
  private final UserService userService;

  public RestUserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/userJoin")
  public ResponseEntity<String> userJoin(@RequestBody UserDto userDto) {
    System.out.println(userDto.getUserName());
    userService.joinUser(userDto);

    return new ResponseEntity<>("Join Success", HttpStatusCode.valueOf(200));
  }

  @PostMapping("/userUpdate")
  public ResponseEntity<String> userUpdate(@RequestBody UserDto userDto){
    userService.userUpdate(userDto);
    return new ResponseEntity<>("Update Success", HttpStatusCode.valueOf(200));
  }

  @GetMapping("/userList")
  public Page<UserDto> userList(@PageableDefault(page = 1) Pageable pageable, @RequestParam Map<String,String> params){
    Page<UserDto> userDtoList = userService.userList(pageable, params);

    return userDtoList;
  }

  @PostMapping("/userDetail")
  public UserDto userDetail(@RequestBody UserDto userDto){
    UserDto userDetail = userService.userDetail(userDto);

    return userDetail;
  }

  @PostMapping("/roleList")
  public List<RoleDTO> roleList(@RequestBody List<Long> roleIds){

    List<RoleDTO> roleDTOList = userService.roleList(roleIds);

    return roleDTOList;
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> userDelete(@PathVariable Long id) {
    try {
      userService.userDelete(id);
    }catch (Exception e){
      e.printStackTrace();
    }

    return new ResponseEntity<>("Delete Success", HttpStatusCode.valueOf(200));
  }


}
