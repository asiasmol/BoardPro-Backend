package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.cardList.CardListDTO;
import com.BoardPro.BoardPro.user.UserDTO;

import java.util.Set;

public record BoardDTO(
        Long id,
        String title,
        UserDTO owner,
        Set<UserDTO> users,
        Set<CardListDTO> cardLists

) {


}
