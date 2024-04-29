package com.board.dto;

import com.board.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentFormDto {
    private Long id;

    @NotBlank(message = "내용은 필수 입력입니다.")
    private String content;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private String createdBy;

    private String modifiedBy;

    private static ModelMapper modelMapper = new ModelMapper();

    //dto -> entity
    public Comment createComment() {
        return modelMapper.map(this, Comment.class);
    }
    //entity -> dto
    public static CommentFormDto of(Comment comment) {
        return modelMapper.map(comment, CommentFormDto.class);
    }

}
