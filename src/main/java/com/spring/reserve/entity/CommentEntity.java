package com.spring.reserve.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.reserve.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 20, nullable = false)
  private String commentWriter;

  @Column
  private String commentContents;

  /* Board:Comment = 1:N */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  @JsonIgnore
  private BoardEntity boardEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private UserEntity userEntity;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_comment_id")
  @JsonIgnore
  private CommentEntity parentCommentEntity;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "root_comment_id")
  @JsonIgnore
  private CommentEntity rootCommentEntity;

  @Transient
  public final List<CommentEntity> childrenComments = new ArrayList<CommentEntity>();

  public static CommentEntity toSaveEntity(CommentDTO commentDTO, BoardEntity boardEntity, CommentEntity parentCommentEntity, CommentEntity rootCommentEntity) {
    return CommentEntity.builder()
            .commentWriter(commentDTO.getCommentWriter())
            .commentContents(commentDTO.getCommentContents())
            .boardEntity(boardEntity)
            .parentCommentEntity(parentCommentEntity)
            .rootCommentEntity(rootCommentEntity)
            .build();

  }

  public static CommentEntity toUpdateEntity(CommentDTO commentDTO) {
    return CommentEntity.builder()
            .commentWriter(commentDTO.getCommentWriter())
            .commentContents(commentDTO.getCommentContents())
            .build();
  }
}
