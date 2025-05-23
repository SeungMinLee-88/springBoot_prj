package com.spring.reserve.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.reserve.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// DB의 테이블 역할을 하는 클래스
@Entity
@Getter
/*@Setter*/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class BoardEntity extends BaseEntity {
  @Id // pk 컬럼 지정. 필수
  @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
  private Long id;

  @Column(length = 20, nullable = false)
  private String boardWriter;

  @Column
  private String boardTitle;

  @Column(length = 500)
  private String boardContents;

  @Column
  private int boardHits;

  @Column
  private int fileAttached;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private UserEntity userEntity;

  @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = false, fetch = FetchType.LAZY)
  private final List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

  @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = false, fetch = FetchType.LAZY)
  private final List<CommentEntity> commentEntityList = new ArrayList<>();

  public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
    return BoardEntity.builder()
            .id(boardDTO.getId())
            .boardWriter(boardDTO.getBoardWriter())
/*            .boardPass(boardDTO.getBoardPass())*/
            .boardTitle(boardDTO.getBoardTitle())
            .boardContents(boardDTO.getBoardContents())
            .fileAttached(boardDTO.getFileAttached()) // 파일 있음.
            .boardHits(0)
            .build();
  }

  public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {

    return BoardEntity.builder()
            .id(boardDTO.getId())
            .boardWriter(boardDTO.getBoardWriter())
/*            .boardPass(boardDTO.getBoardPass())*/
            .boardTitle(boardDTO.getBoardTitle())
            .boardContents(boardDTO.getBoardContents())
            .boardHits(boardDTO.getBoardHits())
            .fileAttached(boardDTO.getFileAttached()) // 파일 있음.
            .build();
  }

/*
  public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
    return BoardEntity.builder()
            .id(boardDTO.getId())
            .boardWriter(boardDTO.getBoardWriter())
            .boardPass(boardDTO.getBoardPass())
            .boardTitle(boardDTO.getBoardTitle())
            .boardContents(boardDTO.getBoardContents())
            .boardHits(boardDTO.getBoardHits())
            .fileAttached(1)
            .build();
  }
*/
/*
 no user builder
*/

  /*public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setBoardWriter(boardDTO.getBoardWriter());
    boardEntity.setBoardPass(boardDTO.getBoardPass());
    boardEntity.setBoardTitle(boardDTO.getBoardTitle());
    boardEntity.setBoardContents(boardDTO.getBoardContents());
    boardEntity.setBoardHits(0);

    BoardDTO boardDTO1 = new BoardDTO();

    return boardEntity;

  }

  public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setId(boardDTO.getId());
    boardEntity.setBoardWriter(boardDTO.getBoardWriter());
    boardEntity.setBoardPass(boardDTO.getBoardPass());
    boardEntity.setBoardTitle(boardDTO.getBoardTitle());
    boardEntity.setBoardContents(boardDTO.getBoardContents());
    boardEntity.setBoardHits(boardDTO.getBoardHits());
    return boardEntity;

  }

  public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setBoardWriter(boardDTO.getBoardWriter());
    boardEntity.setBoardPass(boardDTO.getBoardPass());
    boardEntity.setBoardTitle(boardDTO.getBoardTitle());
    boardEntity.setBoardContents(boardDTO.getBoardContents());
    boardEntity.setBoardHits(0);
    boardEntity.setFileAttached(1); // 파일 있음.
    return boardEntity;
  }*/

}
