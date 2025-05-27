package com.spring.reserve.service.impl;

import com.spring.reserve.dto.CommentDTO;
import com.spring.reserve.entity.BoardEntity;
import com.spring.reserve.entity.CommentEntity;
import com.spring.reserve.repository.BoardRepository;
import com.spring.reserve.repository.CommentRepository;
import com.spring.reserve.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CommentServiceImpl implements CommentService {


  CommentRepository commentRepository;
  BoardRepository boardRepository;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository, BoardRepository boardRepository){
    this.commentRepository = commentRepository;
    this.boardRepository = boardRepository;
  }

  @Override
  public List<CommentDTO> commentList(Long boardId) {
    List<CommentDTO> commentDTOList = new CommentDTO().getChildrencomments();

    return commentDTOList;
  }

  @Override
  public void commentSave(CommentDTO commentDTO) {
    BoardEntity boardEntity = boardRepository.findById(commentDTO.getBoardId()).get();
    Optional<CommentEntity> optionalParentCommentEntity = commentRepository.findById(commentDTO.getParentCommentId());

    Optional<CommentEntity> optionalRootCommentEntity = commentRepository.findById(commentDTO.getRootCommentId());

    if(commentDTO.getIsRootComment().equals("true")){
      CommentEntity saveCommentEntity = new CommentEntity().builder()
              .commentWriter(commentDTO.getCommentWriter())
              .commentContents(commentDTO.getCommentContents())
              .boardEntity(boardEntity).build();
      commentRepository.save(saveCommentEntity);
    }else{
      CommentEntity saveCommentEntity = new CommentEntity().builder()
              .commentWriter(commentDTO.getCommentWriter())
              .commentContents(commentDTO.getCommentContents())
              .boardEntity(boardEntity)
              .parentCommentEntity(optionalParentCommentEntity.get())
              .rootCommentEntity(optionalRootCommentEntity.get()).build();
      commentRepository.save(saveCommentEntity);
    }
  }

  @Override
  public void commentUpdate(CommentDTO commentDTO) {
  }

}
