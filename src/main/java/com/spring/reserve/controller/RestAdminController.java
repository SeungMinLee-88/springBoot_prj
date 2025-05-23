package com.spring.reserve.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@ResponseBody
public class RestAdminController {
  @GetMapping("/manageUser")
  public String adminP() {
    System.out.println("call admin Controller");

    return "admin Controller";
  }
}
