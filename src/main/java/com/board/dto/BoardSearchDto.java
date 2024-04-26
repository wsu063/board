package com.board.dto;

import com.board.constant.BoardTypeStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchDto {
    private String searchDateType;
    private BoardTypeStatus searchTypeStatus;
    private String searchBy;
    private String searchQuery = "";
}
