package com.board.service;

import com.board.dto.CommentFormDto;
import com.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    //insert
    public Long saveComment(CommentFormDto commentFormDto) throws Exception {



        return commentFormDto.getId();
    }
}
