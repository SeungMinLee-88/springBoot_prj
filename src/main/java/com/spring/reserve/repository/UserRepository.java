package com.spring.reserve.repository;

import com.spring.reserve.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
  Boolean existsByLoginId(String loginId);

  UserEntity findByLoginId(String loginId);

  @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roleUserEntities")
  Page<UserEntity> findAllWithRelated(Pageable pageable);

  @Query("SELECT u FROM UserEntity u")
  Page<UserEntity> findAllWithPageble(Specification specification, Pageable pageable);
}
