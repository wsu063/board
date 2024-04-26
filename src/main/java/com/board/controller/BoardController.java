package com.board.controller;

import com.board.dto.BoardFormDto;
import com.board.dto.BoardSearchDto;
import com.board.entity.Board;
import com.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping(value = "/posts/write")
    public String write(Model model) {
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "post/write";
    }

    @PostMapping(value = "/posts/write")
    public String insertBoard(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
                              Model model, @RequestParam("boardImgFile")List<MultipartFile> boardImgFileList) {

        if(bindingResult.hasErrors()) return "post/write";

        try {
            boardService.saveBoard(boardFormDto, boardImgFileList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage"
                    , "상품 등록 중 에러가 발생했습니다.");
            return "post/write";
        }

        return "redirect:/";
    }

    //수정 페이지 보기
    @GetMapping(value = "/posts/rewrite/{boardId}")
    private String boardDtl(@PathVariable("boardId") Long boardId, Model model) {
        try {
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "상품정보를 가져오는 도중 에러가 발생했습니다.");

            //에러발생시 비어있는 객체를 넘겨준다.
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "post/rewrite"; // 수정화면으로 이동
        }
        return "post/rewrite";
    }

    //글 수정(update)
    @PostMapping(value = "/posts/rewrite/{boardId}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, Model model,
                              BindingResult bindingResult,
                              @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList,
                              @PathVariable("boardId") Long boardId) {
        if (bindingResult.hasErrors()) return "post/write"; // rewrite? write?

        BoardFormDto getBoardFormDto = boardService.getBoardDtl(boardId);

        try {
            boardService.updateBoard(boardFormDto, boardImgFileList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage",
                    "상품 수정 중 에러가 발생했습니다.");
            //에러 발생시 비어있는 객체를 넘겨준다.
            model.addAttribute("boardFormDto", getBoardFormDto);
            return "post/rewrite";
        }


        return "redirect:/";
    }

    //글 목록
    @GetMapping(value = {"/posts/list", "posts/list/{page}"})
    public String boardList(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page,
                            Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<Board> boards = boardService.getBoardPage(boardSearchDto,pageable);

        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);

        model.addAttribute("maxPage", 5);

        return "post/list";
    }

    @GetMapping(value = "/posts/detail/{boardId}")
    private String detail(Model model, @PathVariable(value = "boardId")Long boardId) {
        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);

        model.addAttribute("board", boardFormDto);
        return "post/detail";
    }
}
