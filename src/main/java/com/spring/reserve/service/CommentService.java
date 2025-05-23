package com.spring.reserve.service;

import com.spring.reserve.dto.CommentDTO;

import java.util.List;

public interface CommentService {
  List<CommentDTO> commentList(Long boardId);

  void commentSave(CommentDTO commentDTO);

  void commentUpdate(CommentDTO commentDTO);

}
