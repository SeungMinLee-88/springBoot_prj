package com.spring.reserve.repository;

import com.spring.reserve.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findByIdNotIn(List<Long> roleIds);

}
