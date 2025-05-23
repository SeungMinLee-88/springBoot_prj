package com.spring.reserve.service;

import com.spring.reserve.dto.BoardDTO;
import com.spring.reserve.dto.BoardFileDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface BoardService {

  BoardDTO boardSaveAtta(BoardDTO boardDTO) throws IOException ;

  List<BoardDTO> findAll();

  void updateHits(Long id);

  BoardDTO boardDetail(Long id);

  List<BoardFileDTO> fileList(Long boardId);

  List<BoardFileDTO> fileDelete(Long fileId, Long boardId);


  Resource fetchFileAsResource(String fileName) throws FileNotFoundException ;

  void updateBoard(BoardDTO boardDTO) throws IOException ;

  void boardDelete(Long id);

  Page<BoardDTO> boardList(Pageable pageable, Map<String, String> params);

  BoardDTO save(BoardDTO boardDTO) throws IOException ;

  BoardDTO update(BoardDTO boardDTO);

  Page<BoardDTO> paging(Pageable pageable);

}
