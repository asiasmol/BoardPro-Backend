package com.BoardPro.BoardPro.cardList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardListRepository extends JpaRepository<CardList, Long> {

}
