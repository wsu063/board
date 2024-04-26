package com.board.dto;

import com.board.entity.Board;
import com.board.entity.BoardImg;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;


@Getter
@Setter
public class BoardImgDto {
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYN;

    private static ModelMapper modelMapper = new ModelMapper();

    // entity -> dto
    public static BoardImgDto of(BoardImg boardImg) {
        return modelMapper.map(boardImg, BoardImgDto.class); // BoardImgDto 객체를 리턴
    }



}
