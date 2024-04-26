package com.board.service;

import com.board.entity.BoardImg;
import com.board.repository.BoardImgRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardImgService {

    @Value("${boardImgLocation}")
    private String boardImgLocation;

    private final BoardImgRepository boardImgRepository;
    private final FileService fileService;

    public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception {
        String oriImgName = boardImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        if (!StringUtils.isEmpty(oriImgName)) { // 빈 문자열인지 아닌지 검사
            //빈 문자열이 아니면 업로드
            imgName = fileService.uploadFile(boardImgLocation,
                    oriImgName, boardImgFile.getBytes());
            //itemImgFile.getBytes(): 이미지 파일을 byte배열로 만들어준다.

            imgUrl = "/images/post/" + imgName;
        }
        //DB에 insert를 하기전 유저가 직접 입력하지 못하는 값들을 개발자가 넣어준다.
        boardImg.updateBoardImg(oriImgName, imgName, imgUrl);
        boardImgRepository.save(boardImg); // insert
    }

    //이미지 수정
    public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception {
        if(!boardImgFile.isEmpty()) { // 첨부한 이미지 파일이 있다면
            //1. 서버있는 이미지 수정
            BoardImg savedBoardImg = boardImgRepository.findById(boardImgId)
                    .orElseThrow(EntityExistsException::new);

            if(!StringUtils.isEmpty(savedBoardImg.getImgName())) {
                fileService.deleteFile(boardImgLocation + "/" + savedBoardImg.getImgName());
            }

            String oriImgName = boardImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
            String imgUrl = "/images/post/" + imgName;

            //2. board_img 테이블 수정
            savedBoardImg.updateBoardImg(oriImgName, imgName, imgUrl);

        }
    }

}

