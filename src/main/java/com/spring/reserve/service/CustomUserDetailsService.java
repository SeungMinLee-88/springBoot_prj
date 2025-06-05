package com.spring.reserve.service;

import com.spring.reserve.entity.RoleEntity;
import com.spring.reserve.entity.RoleUserEntity;
import com.spring.reserve.entity.UserEntity;
import com.spring.reserve.repository.RoleUserRepository;
import com.spring.reserve.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService  implements UserDetailsService {



  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleUserRepository roleUserRepository;




    @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    System.out.println("call loadUserByUsername");

    UserEntity userEntity = userRepository.findByLoginId(loginId);

    if (userEntity != null) {
      List<RoleEntity> roleEntity = new ArrayList<>();

      List<String> roles = new ArrayList<>();
      List<RoleUserEntity> roleUserEntityList = roleUserRepository.findByUserEntity(userEntity);
      ModelMapper mapper = new ModelMapper();
      for(int i=0; i < roleUserEntityList.size(); i++) {
        roles.add(roleUserEntityList.get(i).getRoleEntity().getRoleName());
      }
      return new CustomUserDetails(userEntity, roles);
    }

    return null;
  }
}