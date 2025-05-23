package com.spring.reserve.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.reserve.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String loginId;
  private String userName;
  private String userPassword;

  @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = false, fetch = FetchType.LAZY)
  private final List<CommentEntity> commentEntityList = new ArrayList<>();

  @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = false, fetch = FetchType.LAZY)
  private final List<BoardEntity> boardEntityList = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<ReserveEntity> reserveEntities = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<RoleUserEntity> roleUserEntities = new ArrayList<>();

  public static UserEntity toSaveEntity(UserDto userDto) {
    return UserEntity.builder()
            .id(userDto.getId())
            .loginId(userDto.getLoginId())
            .userName(userDto.getUserName())
            .userPassword(userDto.getUserPassword())
            .build();
  }
}
