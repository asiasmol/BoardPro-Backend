package com.BoardPro.BoardPro.cardList;

import com.BoardPro.BoardPro.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardListRepository extends JpaRepository<CardList, Long> {

}
