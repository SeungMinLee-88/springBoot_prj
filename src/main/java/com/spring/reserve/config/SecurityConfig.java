package com.spring.reserve.config;

import com.spring.reserve.component.JWTUtil;
import com.spring.reserve.filter.CustomLogoutFilter;
import com.spring.reserve.filter.JWTFilter;
import com.spring.reserve.filter.LoginFilter;
import com.spring.reserve.repository.RefreshRepository;
import com.spring.reserve.repository.RoleRepository;
import com.spring.reserve.repository.RoleUserRepository;
import com.spring.reserve.repository.UserRepository;
import com.spring.reserve.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;
/*  @Autowired
  private final AuthenticationManager authenticationManager;*/
  private final JWTUtil jwtUtil;
  private final RefreshRepository refreshRepository;
  private final RoleRepository roleRepository;
  private final RoleUserRepository roleUserRepository;
  private final UserRepository userRepository;
  private final CustomUserDetailsService customUserDetailsService;

  public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshRepository refreshRepository, RoleRepository roleRepository, RoleUserRepository roleUserRepository, UserRepository userRepository, CustomUserDetailsService customUserDetailsService) {
    this.authenticationConfiguration = authenticationConfiguration;
      this.jwtUtil = jwtUtil;
    this.refreshRepository = refreshRepository;
    this.roleRepository = roleRepository;
    this.roleUserRepository = roleUserRepository;
    this.userRepository = userRepository;
      this.customUserDetailsService = customUserDetailsService;
  }
/*  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    System.out.println("configuration : " + configuration.getAuthenticationManager());
    return configuration.getAuthenticationManager();
  }*/

  @Bean
  public AuthenticationManager authenticationManager(
          BCryptPasswordEncoder bCryptPasswordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(customUserDetailsService);
    authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
    return new ProviderManager(authenticationProvider);
  }


  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    System.out.println("call filterChain");
    http
            .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
              @Override
              public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000, http://localhost:3100"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                configuration.setExposedHeaders(Collections.singletonList("access"));
                return configuration;
              }
            })));

    //csrf disable
    http.csrf((auth) -> auth.disable());

    http.formLogin((auth) -> auth.disable());

    http.httpBasic((auth) -> auth.disable());
    http
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers(
                             "/"
                            , "/join"
                            ,"/api/v1/user/login"
                            ,"/api/v1/user/reIssueToken"
                            , "/api/v1/board/boardList"
                            , "/api/v1/board/detal/*"
                            , "/api/v1/comment/commentList"
                            , "/api/v1/user/userJoin"
                            , "/error").permitAll()
                    .requestMatchers("/api/v1/admin/*").hasAnyRole("ADMIN", "MANAGER")
                    .anyRequest().authenticated());

    http
            .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

    http
            .addFilterAt(new LoginFilter(authenticationManager(bCryptPasswordEncoder()), jwtUtil, refreshRepository, roleUserRepository, userRepository), UsernamePasswordAuthenticationFilter.class);

    http
            .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

    http.sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
