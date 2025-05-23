package com.spring.reserve.repository;

import com.spring.reserve.entity.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeRepository extends JpaRepository<TimeEntity, Long> {

    @Query(value ="select time.id, time.time, nvl2(reserve_time.time_id, '1', '0') reserved " +
            ",(select reserve_user_id from reserve where id = reserve_time.reserve_id ) reserve_user_id " +
            "from time left outer join reserve_time " +
            "on time.id = reserve_time.time_id and reserve_time.reserve_date = :reserveDate"
            , countQuery = "select count(*) from time left outer join reserve_time " +
            "on time.id = reserve_time.time_id and reserve_time.reserve_date = :reserveDate"
            ,nativeQuery = true)
    List<TimeEntity> findByReserveDate(String reserveDate);
}
