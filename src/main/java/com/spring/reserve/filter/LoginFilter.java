package com.spring.reserve.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.reserve.component.JWTUtil;
import com.spring.reserve.dto.RoleDTO;
import com.spring.reserve.dto.UserDto;
import com.spring.reserve.entity.RefreshEntity;
import com.spring.reserve.entity.RoleUserEntity;
import com.spring.reserve.entity.UserEntity;
import com.spring.reserve.repository.RefreshRepository;
import com.spring.reserve.repository.RoleRepository;
import com.spring.reserve.repository.RoleUserRepository;
import com.spring.reserve.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {


  private ObjectMapper objectMapper =  new ObjectMapper();


  private final AuthenticationManager authenticationManager;
  private final RefreshRepository refreshRepository;
  private final JWTUtil jwtUtil;
  private final RoleUserRepository roleUserRepository;
  private final UserRepository userRepository;

  public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository, RoleUserRepository roleUserRepository, UserRepository userRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.refreshRepository = refreshRepository;
    this.roleUserRepository = roleUserRepository;
    this.userRepository = userRepository;
    setFilterProcessesUrl("/api/v1/user/login");

  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    ServletInputStream inputStream = null;
    try {
      inputStream = request.getInputStream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String messageBody = null;
    try {
      messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    UserDto userDto = new UserDto();
    String loginId = "";
    String userPassword = "";
    try {
      userDto = objectMapper.readValue(messageBody, UserDto.class);
      loginId = userDto.getLoginId();
      userPassword = userDto.getUserPassword();
      ModelMapper mapper = new ModelMapper();

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findByLoginId(loginId));
    /*if(optionalUserEntity.isPresent()) {*/
      UserEntity userEntity = optionalUserEntity.get();

      List<RoleUserEntity> roleUserEntityList = roleUserRepository.findByUserEntity(userEntity);

      RoleDTO roleDTO = new RoleDTO();
      ModelMapper mapper = new ModelMapper();

      List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
      for(int i=0; i < roleUserEntityList.size(); i++) {
        roleDTO = mapper.map(roleUserEntityList.get(i).getRoleEntity()
                , new TypeToken<RoleDTO>() {
                }.getType());
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleDTO.getRoleName());
        updatedAuthorities.add(authority);
      }

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, userPassword, updatedAuthorities);

    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(401);
  }

  protected void successfulAuthentication (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

    String userName = authentication.getName();


    List<String> role = new ArrayList<>();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    while ( iterator.hasNext()){
      role.add(String.valueOf(iterator.next()));
    }

    System.out.println("userRole : " + role);

    /*
    100000L 20000L 600000L 86400000L
    */
    String access = jwtUtil.createJwt("access", userName, role, 86400000L);
    String refresh = jwtUtil.createJwt("refresh", userName, role, 86400000L);
    addRefreshEntity(userName, refresh, 20000L);

    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, userName, Response-Header, access" );
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS" );
    response.setHeader("Access-Control-Allow-Origin", "localhost:3000" );
    response.setHeader("Access-Control-Expose-Headers", "userName, access" );
    response.setHeader("access", access );
    response.setHeader("userName", userName );
    response.addCookie(createCookie("refresh", refresh));
    response.setStatus(HttpStatus.OK.value());
  }

  private void addRefreshEntity(String username, String refresh, Long expiredMs) {

    Date date = new Date(System.currentTimeMillis() + expiredMs);
    RefreshEntity refreshEntity = new RefreshEntity();
    refreshEntity.setUsername(username);
    refreshEntity.setRefresh(refresh);
    refreshEntity.setExpiration(date.toString());

    refreshRepository.save(refreshEntity);
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
    cookie.setHttpOnly(true);

    return cookie;
  }
}
