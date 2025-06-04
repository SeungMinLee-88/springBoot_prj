package com.spring.reserve.controller;

import com.spring.reserve.dto.*;
import com.spring.reserve.entity.BoardEntity;
import com.spring.reserve.entity.CommentEntity;
import com.spring.reserve.repository.BoardRepository;
import com.spring.reserve.repository.CommentRepository;
import com.spring.reserve.service.CommentService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class RestCommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @GetMapping("/commentList")
    @Transactional(readOnly = true)
    public Page<CommentEntity> commentList(@PageableDefault(page = 1) Pageable pageable, @RequestParam Long boardId) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        Page<CommentEntity> rootCommentEntity = commentRepository.findByBoardEntityAndParentCommentEntityIsNull(boardEntity, PageRequest.of(page, pageable.getPageSize()));


        List<Long> rootCommentIds = rootCommentEntity.stream().map(CommentEntity::getId).collect(Collectors.toList());
        List<CommentEntity> subComments = commentRepository.findAllSubCommentEntitysInRoot(rootCommentIds);

        subComments.forEach(subComment -> {
            subComment.getParentCommentEntity().getChildrenComments().add(subComment); // no
        });

        ModelMapper mapper = new ModelMapper();

        List<CommentDTO> rootCommentDTOList = mapper.map(rootCommentEntity.getContent(), new TypeToken<List<CommentDTO>>() {
        }.getType());

        return rootCommentEntity;
    }

    @GetMapping("/commentGetRoot")
    @Transactional(readOnly = true)
    public CommentDTO commentGetRoot(@RequestParam Long commentId) {

        CommentEntity commentEntity = commentRepository.findById(commentId).get();

        ModelMapper mapper = new ModelMapper();

        CommentDTO commentDTO = mapper.map(commentEntity, new TypeToken<CommentDTO>() {
        }.getType());

        return commentDTO;
    }

    @PostMapping("/commentSave")
    public ResponseEntity<String> commentSave(@RequestBody CommentDTO commentDTO){
        commentService.commentSave(commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body("save success");
    }

    @Transactional
    @PostMapping("/commentUpdate")
    public ResponseEntity<String> commentUpdate(@RequestBody CommentDTO commentDTO) {
        CommentEntity updateCommentEntity = CommentEntity.toUpdateEntity(commentDTO);
        updateCommentEntity.setCommentContents(commentDTO.getCommentContents());
        commentRepository.updateCommentContents(commentDTO.getCommentContents(), commentDTO.getId());

        return ResponseEntity.status(HttpStatus.OK).body("update success");
    }

}










