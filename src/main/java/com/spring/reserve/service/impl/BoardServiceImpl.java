package com.spring.reserve.service.impl;

import com.spring.reserve.dto.BoardDTO;
import com.spring.reserve.dto.BoardFileDTO;
import com.spring.reserve.entity.BoardEntity;
import com.spring.reserve.entity.BoardFileEntity;
import com.spring.reserve.entity.SearchCriteria;
import com.spring.reserve.repository.BoardFileRepository;
import com.spring.reserve.repository.BoardRepository;
import com.spring.reserve.service.BoardService;
import com.spring.reserve.service.BoardSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
  private final BoardRepository boardRepository;
  private final BoardFileRepository boardFileRepository;

  @Override
  public BoardDTO boardSaveAtta(BoardDTO boardDTO) throws IOException {

    ModelMapper mapper = new ModelMapper();
    if (boardDTO.getFileList() == null) {
      boardDTO.setFileAttached(0);
      BoardEntity saveBoardEntity = BoardEntity.toSaveEntity(boardDTO);
      BoardEntity boardEntitys = boardRepository.save(saveBoardEntity);
      BoardDTO boardDTO1  = mapper.map(boardEntitys, new TypeToken<BoardDTO>(){}.getType());
      return boardDTO1;
    } else {
      boardDTO.setFileAttached(1);
      BoardEntity saveBoardEntity = BoardEntity.toSaveEntity(boardDTO);
      BoardEntity boardEntitys = boardRepository.save(saveBoardEntity);
      Long savedId = boardRepository.save(saveBoardEntity).getId();
      BoardEntity board = boardRepository.findById(savedId).get();

      if(boardDTO.getFileList().length > 0) {
        for (MultipartFile boardFile : boardDTO.getFileList()) {
          String originalFilename = boardFile.getOriginalFilename();
          String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
          String savePath = "C:/Users/lsmls/IdeaProjects/springBoot_prj/attached/" + storedFileName;
          String mimeType = boardFile.getContentType().substring(0, boardFile.getContentType().indexOf("/"));
          boardFile.transferTo(new File(savePath)); // 5.
          BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName, mimeType);
          boardFileRepository.save(boardFileEntity);
        }
      }
      BoardDTO boardDTO1  = mapper.map(boardEntitys, new TypeToken<BoardDTO>(){}.getType());
      return boardDTO1;
      }

  }

  @Override
  @Transactional
  public List<BoardDTO> findAll() {
    List<BoardEntity> boardEntityList = boardRepository.findAll();
    List<BoardDTO> boardDTOList = new ArrayList<>();
    for (BoardEntity boardEntity: boardEntityList) {
      boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
    }
    return boardDTOList;
  }

  @Override
  @Transactional
  public void updateHits(Long id) {
    boardRepository.updateHits(id);
  }

  @Override
  @Transactional
  public BoardDTO boardDetail(Long id) {
    Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
    if (optionalBoardEntity.isPresent()) {
      BoardEntity boardEntity = optionalBoardEntity.get();

      BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
      if (boardDTO.getFileAttached() == 1) {
        ModelMapper mapper = new ModelMapper();
        List<BoardFileDTO> boardFileDTOList = mapper.map(boardEntity.getBoardFileEntityList(), new TypeToken<List<BoardFileDTO>>() {
        }.getType());
        boardDTO.setBoardFileDTO(boardFileDTOList);
      }
      return boardDTO;
    } else {
      return null;
    }
  }

  @Override
  @Transactional
  public List<BoardFileDTO> fileList(Long boardId) {
    List<BoardFileEntity> boardFileEntityList = boardFileRepository.findByBoardId(boardId);
        ModelMapper mapper = new ModelMapper();
        List<BoardFileDTO> fileDTOList = mapper.map(boardFileEntityList, new TypeToken<List<BoardFileDTO>>() {
        }.getType());

      return fileDTOList;
  }

  @Transactional
  public List<BoardFileDTO> fileDelete(Long fileId, Long boardId) {
    boardFileRepository.deleteById(fileId);

    List<BoardFileEntity> boardFileEntityList = boardFileRepository.findByBoardId(boardId);

    ModelMapper mapper = new ModelMapper();
    List<BoardFileDTO> fileDTOList = mapper.map(boardFileEntityList, new TypeToken<List<BoardFileDTO>>() {
    }.getType());

    if(boardFileEntityList.size() == 0)
    {
      boardRepository.updatefileAttached(boardId);
    }

    return fileDTOList;
  }

  @Override
  public Resource fetchFileAsResource(String fileName) throws FileNotFoundException {
    Path UPLOAD_PATH;
    try {
        UPLOAD_PATH = Paths.get("C:\\Users\\lsmls\\IdeaProjects\\springBoot_prj\\attached");
        Path filePath = UPLOAD_PATH.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new FileNotFoundException("File not found " + fileName);
    }
  }

  @Override
  public void updateBoard(BoardDTO boardDTO) throws IOException {

    List<BoardFileEntity> boardFileEntityList = boardFileRepository.findByBoardId(boardDTO.getId());
    ModelMapper mapper = new ModelMapper();
    List<BoardFileDTO> fileDTOList = mapper.map(boardFileEntityList, new TypeToken<List<BoardFileDTO>>() {
    }.getType());

    if (boardDTO.getFileList() == null && boardFileEntityList.size() == 0) {
      boardDTO.setFileAttached(0);
      BoardEntity saveBoardEntity = BoardEntity.toSaveEntity(boardDTO);
      BoardEntity boardEntitys = boardRepository.save(saveBoardEntity);

      BoardDTO boardDTO1  = mapper.map(boardEntitys, new TypeToken<BoardDTO>(){}.getType());
    } else {
      boardDTO.setFileAttached(1);
      BoardEntity saveBoardEntity = BoardEntity.toSaveEntity(boardDTO);

      Long savedId = boardRepository.save(saveBoardEntity).getId();
      BoardEntity board = boardRepository.findById(savedId).get();

      if(boardDTO.getFileList() != null && boardDTO.getFileList().length > 0 ||  boardFileEntityList.size() == 0) {

        for (MultipartFile boardFile : boardDTO.getFileList()) {

          String originalFilename = boardFile.getOriginalFilename(); // 2.
          String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
          String savePath = "C:/Users/lsmls/IdeaProjects/springBoot_prj/attached/" + storedFileName;

          boardFile.transferTo(new File(savePath)); // 5.
          String mimeType = boardFile.getContentType().substring(0, boardFile.getContentType().indexOf("/"));
          BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName, mimeType);
          boardFileRepository.save(boardFileEntity);
        }
      }
    }
  }

  @Override
  public void boardDelete(Long id) {
    boardRepository.deleteById(id);
  }

  @Override
  public Page<BoardDTO> pagingList(Pageable pageable, Map<String, String> params){
    int page = pageable.getPageNumber() - 1;
    //int pageLimit = 3;

    Specification<BoardEntity> specification = new BoardSpecification(new SearchCriteria(params.get("searchKey"), params.get("searchValue")));
    /*Page<BoardEntity> boardEntities = boardRepository.findAll(specification, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));*/
    Page<BoardEntity> boardEntities = boardRepository.findAll(specification, PageRequest.of(page, pageable.getPageSize(), pageable.getSort()));

    Page<BoardDTO> boardDTOList = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));

    return boardDTOList;

  }

  @Override
  public BoardDTO save(BoardDTO boardDTO) throws IOException {

    BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
    BoardEntity boardEntitys = boardRepository.save(boardEntity);
    ModelMapper mapper = new ModelMapper();
    BoardDTO boardDTO1  = mapper.map(boardEntitys, new TypeToken<BoardDTO>(){}.getType());
    return boardDTO1;
  }

  @Override
  public BoardDTO update(BoardDTO boardDTO) {
    BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
    boardRepository.save(boardEntity);
    return boardDetail(boardDTO.getId());
  }

  @Override
  public Page<BoardDTO> paging(Pageable pageable){
    int page = pageable.getPageNumber() - 1;
    int pageLimit = 3;
    Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
    Page<BoardDTO> boardDTOList = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));

    return boardDTOList;

  }

}
