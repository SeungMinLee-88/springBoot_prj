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


  /*@OneToMany(mappedBy = "commentEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Set<CommentEntity> commentEntityList;*/
/*  @OneToMany(mappedBy = "commentEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<CommentEntity> commentEntityList = new ArrayList<>();*/

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_comment_id")
  @JsonIgnore
  private CommentEntity parentCommentEntity;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "root_comment_id")
  @JsonIgnore
  private CommentEntity rootCommentEntity;

  @Transient
  public List<CommentEntity> childrenComments = new ArrayList<CommentEntity>();

  public static CommentEntity toSaveEntity(CommentDTO commentDTO, BoardEntity boardEntity, CommentEntity parentCommentEntity, CommentEntity rootCommentEntity) {
    CommentEntity commentEntity = new CommentEntity();
    commentEntity.setCommentWriter(commentDTO.getCommentWriter());
    commentEntity.setCommentContents(commentDTO.getCommentContents());
    commentEntity.setBoardEntity(boardEntity);
    commentEntity.setParentCommentEntity(parentCommentEntity);
    commentEntity.setRootCommentEntity(rootCommentEntity);
    return commentEntity;
  }
  public static CommentEntity toUpdateEntity(CommentDTO commentDTO) {
    CommentEntity commentEntity = new CommentEntity();
    commentEntity.setId(commentDTO.getId());
    commentEntity.setCommentContents(commentDTO.getCommentContents());
    return commentEntity;
  }

/*  public static List<CommentEntity> tocommentList(CommentDTO commentDTO, BoardEntity boardEntity) {
    List<CommentEntity> commentEntity = new CommentEntity();
    commentEntity.setCommentWriter(commentDTO.getCommentWriter());
    commentEntity.setCommentContents(commentDTO.getCommentContents());
    commentEntity.setBoardEntity(boardEntity);
    return commentEntity;
  }*/
}
