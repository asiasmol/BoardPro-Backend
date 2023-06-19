package com.BoardPro.BoardPro.cardList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardListRepository extends JpaRepository<CardList, Long> {

}
