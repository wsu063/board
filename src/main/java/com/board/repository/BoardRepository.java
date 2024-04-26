package com.board.repository;

import com.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardRepository extends JpaRepository<Board, Long>,
        QuerydslPredicateExecutor<Board>, BoardRepositoryCustom {

}
