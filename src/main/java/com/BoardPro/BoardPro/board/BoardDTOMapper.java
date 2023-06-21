package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.cardList.CardListDTOMapper;
import com.BoardPro.BoardPro.user.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardDTOMapper implements Function<Board, BoardDTO> {

    private final UserDTOMapper userDTOMapper;
    private final CardListDTOMapper cardListDTOMapper;

    @Override
    public BoardDTO apply(Board board) {
        return new BoardDTO(board.getId(), board.getTitle(), userDTOMapper.apply(board.getOwner()), board.getUsers().stream().map(userDTOMapper).collect(Collectors.toSet()), board.getCardLists().stream().map(cardListDTOMapper).collect(Collectors.toSet()));
    }
}
