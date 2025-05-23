package com.spring.reserve.repository;

import com.spring.reserve.entity.HallEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<HallEntity, Long> {
}
