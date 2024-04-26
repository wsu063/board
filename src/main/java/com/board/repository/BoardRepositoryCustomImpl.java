package com.board.repository;

import com.board.constant.BoardTypeStatus;
import com.board.dto.BoardSearchDto;
import com.board.entity.Board;
import com.board.entity.QBoard;
import com.board.entity.QBoardImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{
    
    private JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    //현재 날짜로부터 이전 날짜를 구해주는 메소드
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now(); // 현재 날짜, 시간

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QBoard.board.regTime.after(dateTime); // 몇일전 이후부터
    }

    //전체검색할때 null이 있으므로 처리한다
    private BooleanExpression searchTypeStatusEq(BoardTypeStatus boardTypeStatus) {
        return boardTypeStatus == null ? null : QBoard.board.boardTypeStatus.eq(boardTypeStatus);
    }
    
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("title", searchBy)) { // 글제목 검색
            return QBoard.board.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchQuery)) { // 작성자 검색시
            return QBoard.board.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    private BooleanExpression titleLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QBoard.board.title.like("%" + searchQuery + "%");
    }

    @Override
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {

        List<Board> content = queryFactory
                .selectFrom(QBoard.board)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchTypeStatusEq(boardSearchDto.getSearchTypeStatus()),
                        searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(QBoard.board)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchTypeStatusEq(boardSearchDto.getSearchTypeStatus()),
                        searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .fetchOne();

        //pageable 객체: 한 페이지의 몇개의 게시물을 보여줄지, 시작페이지 번호에 대한 정보를 가지고 있다.
        return new PageImpl<>(content, pageable, total);
    }
}
