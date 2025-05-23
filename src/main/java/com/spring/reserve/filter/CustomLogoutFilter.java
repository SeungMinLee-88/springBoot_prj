package com.spring.reserve.filter;

import com.spring.reserve.component.JWTUtil;
import com.spring.reserve.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;

  public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
    this.jwtUtil = jwtUtil;
    this.refreshRepository = refreshRepository;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
  }

  private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

    String requestUri = request.getRequestURI();
    if (!requestUri.matches("/api/v1/user/logout")) {

      filterChain.doFilter(request, response);
      return;
    }
    String requestMethod = request.getMethod();
    if (!requestMethod.equals("POST")) {

      filterChain.doFilter(request, response);
      return;
    }


    String refresh = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) {
        refresh = cookie.getValue();
      }
    }

    //refresh null check
    if (refresh == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    //expired check
    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    Boolean isExist = refreshRepository.existsByRefresh(refresh);
    if (!isExist) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    //Logout
    refreshRepository.deleteByRefresh(refresh);
    Cookie cookie = new Cookie("refresh", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
    response.setStatus(HttpServletResponse.SC_OK);
  }
}