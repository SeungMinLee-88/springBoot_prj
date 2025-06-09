package com.spring.reserve.dto;

import com.spring.reserve.entity.BaseEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO extends BaseEntity {
  private Long id;
  private String commentWriter;
  private String commentContents;
  private Long boardId;
  private String isRootComment;
  private Long parentCommentId = 0L;
  private Long rootCommentId = 0L;
  private List<CommentDTO> childrencomments;
}