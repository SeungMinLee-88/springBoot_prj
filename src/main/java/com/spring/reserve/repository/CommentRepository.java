package com.spring.reserve.repository;

import com.spring.reserve.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
  @Query("SELECT commentEntity FROM CommentEntity commentEntity "
          + " WHERE commentEntity.parentCommentEntity.id IS NULL")
  public Page<CommentEntity> findAllRoots(@Param("boardId") Long boardId, PageRequest pageRequest);

  Page<CommentEntity> findByBoardEntityAndParentCommentEntityIsNull(BoardEntity boardEntity, PageRequest pageRequest);

  @Query("SELECT commentEntity FROM CommentEntity commentEntity"
          + " WHERE commentEntity.rootCommentEntity.id IN :rootIds ")
  public List<CommentEntity> findAllSubCommentEntitysInRoot(@Param("rootIds") List<Long> rootIds);

  @Modifying
  @Query(value = "update CommentEntity c set c.commentContents=:commentContents where c.id=:id") // 엔티티기준
  public void updateCommentContents(String commentContents, Long id);
}
