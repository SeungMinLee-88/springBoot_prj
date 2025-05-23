package com.spring.reserve.repository;

import com.spring.reserve.entity.RoleUserEntity;
import com.spring.reserve.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleUserRepository extends JpaRepository<RoleUserEntity, Long> {

/*    @Query(value ="SELECT r.* FROM role_user r where user_id = :loginId"
            , countQuery = "select count(*) FROM role_user r where user_id = :loginId"
            ,nativeQuery = true)*/
    @Query("SELECT r FROM RoleUserEntity r JOIN FETCH r.roleEntity WHERE r.userEntity = :userEntity")
    List<RoleUserEntity> findByUserEntity(UserEntity userEntity);

}
