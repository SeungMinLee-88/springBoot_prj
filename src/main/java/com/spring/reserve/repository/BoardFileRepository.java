package com.spring.reserve.repository;

import com.spring.reserve.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Long> {

    @Query(value ="select * from board_file where board_id = :boardId"
            , countQuery = "select count(*) from board_file where board_id = :boardId"
            ,nativeQuery = true)
    List<BoardFileEntity> findByBoardId(long boardId);
}