package com.spring.reserve.service;

import com.spring.reserve.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails  implements UserDetails {

  private final UserEntity userEntity;
  private List<String> userRoles;

  public CustomUserDetails(UserEntity userEntity, List<String> userRoles) {

    this.userEntity = userEntity;
      this.userRoles = userRoles;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collection = new ArrayList<>();

/*    collection.add(new GrantedAuthority() {

      @Override
      public String getAuthority() {
        //return "";
        return userRoles.toString();
      }
    });*/
    List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
    for(int i=0; i < userRoles.size(); i++) {
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRoles.get(i));
      collection.add(authority);
    }

    return collection;
  }

  @Override
  public String getPassword() {

    return userEntity.getUserPassword();
  }

  @Override
  public String getUsername() {

    return userEntity.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {

    return true;
  }

  @Override
  public boolean isAccountNonLocked() {

    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {

    return true;
  }

  @Override
  public boolean isEnabled() {

    return true;
  }
}