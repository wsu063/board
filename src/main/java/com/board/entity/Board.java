package com.board.entity;

import com.board.constant.BoardTypeStatus;
import com.board.dto.BoardFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board extends BaseEntity { // 제목, 내용, 타입(공지 혹은 일반)
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardTypeStatus boardTypeStatus;

    //board엔티티 수정
    public void updateBoard(BoardFormDto boardFormDto) {
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
        this.boardTypeStatus = boardFormDto.getBoardTypeStatus();
    }
}
