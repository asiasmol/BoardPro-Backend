package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.cardList.CardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}
