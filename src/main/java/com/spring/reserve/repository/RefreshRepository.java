package com.spring.reserve.repository;

import com.spring.reserve.entity.RefreshEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

  Boolean existsByRefresh(String refresh);

  @Transactional
  void deleteByRefresh(String refresh);
}