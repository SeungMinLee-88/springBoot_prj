package com.spring.reserve.dto;

import com.spring.reserve.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardDTO {
  private Long id;
  private String boardWriter;
/*  private String boardPass;*/
  private String boardTitle;
  private String boardContents;
  private int boardHits;
  private LocalDateTime boardCreatedTime;
  private LocalDateTime boardUpdatedTime;

  private MultipartFile[] fileList; // save.html -> Controller 파일 담는 용도
  private List<String> originalFileName; // 원본 파일 이름
  private List<String> storedFileName; // 서버 저장용 파일 이름
  private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

  private List<BoardFileDTO> boardFileDTO;

  public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
    this.id = id;
    this.boardWriter = boardWriter;
    this.boardTitle = boardTitle;
    this.boardHits = boardHits;
    this.boardCreatedTime = boardCreatedTime;
  }

  public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
    BoardDTO boardDTO = new BoardDTO();
    boardDTO.setId(boardEntity.getId());
    boardDTO.setBoardWriter(boardEntity.getBoardWriter());
    boardDTO.setBoardTitle(boardEntity.getBoardTitle());
    boardDTO.setBoardContents(boardEntity.getBoardContents());
    boardDTO.setBoardHits(boardEntity.getBoardHits());
    boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
    boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
    boardDTO.setFileAttached(boardEntity.getFileAttached());
    return boardDTO;
  }
}
