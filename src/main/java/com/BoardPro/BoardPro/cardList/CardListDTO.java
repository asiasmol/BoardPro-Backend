package com.BoardPro.BoardPro.cardList;

import com.BoardPro.BoardPro.card.CardDTO;

import java.util.Set;

public record CardListDTO(
        Long id,
        String title,
        Set<CardDTO> cards
) {
}
