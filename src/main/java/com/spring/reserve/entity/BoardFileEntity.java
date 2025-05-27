package com.spring.reserve.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    BoardFileEntity boardFileEntity = BoardFileEntity.builder()
            .originalFileName(originalFileName)
            .storedFileName(storedFileName)
            .mimeType(contentType)
            .boardEntity(boardEntity)
            .build();
    return boardFileEntity;
  }
}
