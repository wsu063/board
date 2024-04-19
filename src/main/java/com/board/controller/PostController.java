package com.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    @GetMapping(value = "/posts/list")
    public String list() {
        return "post/list";
    }

    @GetMapping(value = "/posts/write")
    public String write() {
        return "post/write";
    }

    @GetMapping(value = "/posts/rewrite")
    private String rewrite() {
        return "post/rewrite";
    }

    @GetMapping(value = "/posts/detail")
    private String detail() {
        return "post/detail";
    }
}
