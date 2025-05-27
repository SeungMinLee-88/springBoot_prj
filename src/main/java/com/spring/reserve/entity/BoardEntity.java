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
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
            .boardTitle(boardDTO.getBoardTitle())
            .boardContents(boardDTO.getBoardContents())
            .boardHits(boardDTO.getBoardHits())
            .fileAttached(boardDTO.getFileAttached()) // 파일 있음.
            .build();
  }

}
