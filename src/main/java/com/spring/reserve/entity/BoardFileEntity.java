package com.spring.reserve.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Builder
@Table(name = "board_file_table")
public class BoardFileEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String originalFileName;

  @Column
  private String storedFileName;

  @Column
  private String mimeType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private BoardEntity boardEntity;

  public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName, String contentType) {
/*    BoardFileEntity boardFileEntity = new BoardFileEntity();
    boardFileEntity.setOriginalFileName(originalFileName);
    boardFileEntity.setStoredFileName(storedFileName);
    boardFileEntity.setMimeType(contentType);
    boardFileEntity.setBoardEntity(boardEntity);
    return boardFileEntity;*/
    return BoardFileEntity.builder()
            .originalFileName(originalFileName)
            .storedFileName(storedFileName)
            .mimeType(contentType)
            .boardEntity(boardEntity)
            .build();
  }
}
