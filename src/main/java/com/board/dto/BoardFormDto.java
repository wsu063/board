package com.board.dto;

import com.board.constant.BoardTypeStatus;
import com.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardFormDto {
    private Long id;

    @NotBlank(message = "제목은 필수 입력입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력입니다.")
    private String content;

    private BoardTypeStatus boardTypeStatus;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private String createdBy;
    private String modifiedBy;

    private List<BoardImgDto> boardImgDtoList = new ArrayList<>();

    private List<Long> boardImgIds = new ArrayList<>(); // 이미지 아이디 저장(수정할때씀)

    private static ModelMapper modelMapper = new ModelMapper();

    //dto -> entity
    public Board createBoard() {
        return modelMapper.map(this, Board.class);

    }
    //entity -> dto
    public static BoardFormDto of(Board board) {
        return modelMapper.map(board, BoardFormDto.class);
    }






}
