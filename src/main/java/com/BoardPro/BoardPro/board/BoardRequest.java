package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.cardList.CardList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {
    private String title;
    private Set<CardList> cardLists;
}
