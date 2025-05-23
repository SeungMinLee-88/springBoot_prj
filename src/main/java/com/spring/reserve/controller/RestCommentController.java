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

   /* @GetMapping("/commentList")
    public List<CommentDTO> commentList(@RequestParam Long boardId) {
        System.out.println("boardId : " + boardId);
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<CommentDTO> commentDTOList = commentService.commentList(boardId);
        System.out.println("commentDTOList : " + commentDTOList.toString());
        //model.addAttribute("boardList", boardDTOList);
        return commentDTOList;
    }*/

    @GetMapping("/commentList")
    @Transactional(readOnly = true)
    public Page<CommentEntity> commentTrees(@PageableDefault(page = 1) Pageable pageable, @RequestParam Long boardId) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;

        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        /*Page<CommentEntity> rootCommentEntity = commentRepository.findAllRoots(boardId, PageRequest.of(page, pageable.getPageSize())); // first db call*/
        Page<CommentEntity> rootCommentEntity = commentRepository.findByBoardEntityAndParentCommentEntityIsNull(boardEntity, PageRequest.of(page, pageable.getPageSize()));


        // Now Find all the subcategories
        List<Long> rootCommentIds = rootCommentEntity.stream().map(CommentEntity::getId).collect(Collectors.toList());
        List<CommentEntity> subComments = commentRepository.findAllSubCommentEntitysInRoot(rootCommentIds); // second db call

        subComments.forEach(subComment -> {
            subComment.getParentCommentEntity().getChildrenComments().add(subComment); // no further db call, because everyone inside the root is in the persistence context.
        });
        System.out.println("rootCommentEntity : " + rootCommentEntity.getContent());

        ModelMapper mapper = new ModelMapper();

        List<CommentDTO> rootCommentDTOList = mapper.map(rootCommentEntity.getContent(), new TypeToken<List<CommentDTO>>() {
        }.getType());
        //System.out.println("aaaaaaaaaa  rootCommentDTOList : " + rootCommentDTOList.toString());

        return rootCommentEntity;
    }

    @GetMapping("/commentGetRoot")
    @Transactional(readOnly = true)
    public CommentDTO commentGetRoot(@RequestParam Long commentId) {

        CommentEntity commentEntity = commentRepository.findById(commentId).get();

        ModelMapper mapper = new ModelMapper();

        CommentDTO commentDTO = mapper.map(commentEntity, new TypeToken<CommentDTO>() {
        }.getType());
        //System.out.println("aaaaaaaaaa  rootCommentDTOList : " + commentDTO.toString());

        return commentDTO;
    }

    @PostMapping("/commentSave")
    public ResponseEntity<String> commentSave(@RequestBody CommentDTO commentDTO) throws IOException {
        System.out.println("commentDTO = " + commentDTO);
        System.out.println("PostMapping commentDTO = " + commentDTO.getIsRootComment());
        commentService.commentSave(commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body("save success");
    }

    @Transactional
    @PostMapping("/commentUpdate")
    public ResponseEntity<String> commentUpdate(@RequestBody CommentDTO commentDTO) throws IOException {
        System.out.println("commentUpdate commentDTO = " + commentDTO);


        CommentEntity updateCommentEntity = CommentEntity.toUpdateEntity(commentDTO);
        updateCommentEntity.setCommentContents(commentDTO.getCommentContents());
        commentRepository.updateCommentContents(commentDTO.getCommentContents(), commentDTO.getId());

        return ResponseEntity.status(HttpStatus.OK).body("update success");
    }

}










