package com.spring.reserve.filter;

import com.spring.reserve.component.JWTUtil;
import com.spring.reserve.entity.RoleEntity;
import com.spring.reserve.entity.UserEntity;
import com.spring.reserve.service.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  public JWTFilter(JWTUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String accessToken = request.getHeader("access");
    if (accessToken == null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {

      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {
      //response body
      PrintWriter writer = response.getWriter();
      writer.print("accessToken expired");
      //response status code
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }catch (JwtException e) {

      //response body
      PrintWriter writer = response.getWriter();
      writer.print("accessToken not valid");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      return;
    }

    String category = jwtUtil.getCategory(accessToken);


    if (!category.equals("access")) {
      PrintWriter writer = response.getWriter();
      writer.print("invalid access token");

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String userName = jwtUtil.getUsername(accessToken);
    List<String> userRole = jwtUtil.getRole(accessToken);

    UserEntity userEntity = new UserEntity();
    userEntity.setUserName(userName);
    List<RoleEntity> roleEntity = new ArrayList<>();

    List<String> userRoles = new ArrayList<>();
    userRoles = userRole;

    CustomUserDetails customUserDetails = new CustomUserDetails(userEntity, userRoles);

    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);
    filterChain.doFilter(request, response);

  }
}
