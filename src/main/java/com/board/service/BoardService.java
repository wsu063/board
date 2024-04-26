package com.board.service;

import com.board.dto.BoardFormDto;
import com.board.dto.BoardImgDto;
import com.board.dto.BoardSearchDto;
import com.board.entity.Board;
import com.board.entity.BoardImg;
import com.board.repository.BoardImgRepository;
import com.board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final BoardImgService boardImgService;

    //insert
    public Long saveBoard(BoardFormDto boardFormDto,
                         List<MultipartFile> boardImgFileList) throws Exception {

        // 1. 상품 등록(insert)
        Board board = boardFormDto.createBoard();
        boardRepository.save(board); // insert

        // 2. 이미지 등록
        for (int i = 0; i < boardImgFileList.size(); i++) {
            BoardImg boardImg = new BoardImg();
            boardImg.setBoard(board); // ★itemImg가 item을 참조하므로 반드시 item 객체를 입력해준다.

            //첫번째 이미지를 대표 이미지로 지정
            if(i == 0) {
                boardImg.setRepImgYn("Y");
            } else {
                boardImg.setRepImgYn("N");
            }
            //이미지 파일을 하나씩 저장
            boardImgService.saveBoardImg(boardImg, boardImgFileList.get(i));
        }

        return boardFormDto.getId();
    }

    //글 가져오기
    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardId) {
        //1. board_img 테이블의 이미지를 가져온다.
        List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);

        //BoardImg Entity -> Dto 변환
        List<BoardImgDto> boardImgDtoList = new ArrayList<>();
        for(BoardImg boardImg : boardImgList) {
            BoardImgDto boardImgDto = BoardImgDto.of(boardImg);
            boardImgDtoList.add(boardImgDto);
        }

        //2. board 테이블에 있는 데이터를 가져온다.
        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        //Board Entity -> Dto 변환
        BoardFormDto boardFormDto = BoardFormDto.of(board);

        //3. BoardFormDto에 boardImgDtoList를 넣어준다.
        boardFormDto.setBoardImgDtoList(boardImgDtoList);

        return boardFormDto;
    }

    //글 수정하기
    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception {
        //1, board 엔티티 수정 -> update 전 반드시 select
        Board board = boardRepository.findById(boardFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        //update 실행
        board.updateBoard(boardFormDto);

        //2. board_img 엔티티 수정
        List<Long> boardImgIds = boardFormDto.getBoardImgIds();

        for (int i = 0; i < boardImgFileList.size(); i++) {
            boardImgService.updateBoardImg(boardImgIds.get(i), boardImgFileList.get(i));
        }
        return board.getId();
    }

    @Transactional(readOnly = true)
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        Page<Board> boardPage = boardRepository.getBoardPage(boardSearchDto, pageable);
        return boardPage;
    }


}
