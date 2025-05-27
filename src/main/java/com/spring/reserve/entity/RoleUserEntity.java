package com.spring.reserve.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
/*@Setter*/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_user")
public class RoleUserEntity extends BaseEntity {
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String reserveDate;


    public static RoleUserEntity toSaveEntity(RoleEntity roleEntity, UserEntity userEntity) {
        return RoleUserEntity.builder()
                .roleEntity(roleEntity)
                .userEntity(userEntity)
                .build();
    }
}
