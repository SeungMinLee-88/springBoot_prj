package com.spring.reserve.repository;

import com.spring.reserve.entity.ReserveTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReserveTimeRepository extends JpaRepository<ReserveTimeEntity, Long> {

    @Query(value ="select * from reserve_time where reserve_date = :reserveDate"
            , countQuery = "select count(*) from reserve_time where reserve_date = :reserveDate"
            ,nativeQuery = true)
    List<ReserveTimeEntity> findByReserveDate(String reserveDate);
}
