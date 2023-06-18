package com.BoardPro.BoardPro.cardList;

import com.BoardPro.BoardPro.board.Board;
import com.BoardPro.BoardPro.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardListService {

    private final CardListRepository cardListRepository;
    private final BoardRepository boardRepository;

    public void addListBoard(CardListRequest request, Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new RuntimeException("Board bo found"));
        // To do handle exception in proper elegant way

        CardList cardList = CardList.builder()
                .title(request.getTitle())
                .board(board)
                .build();

        board.getCardLists().add(cardList);
        cardListRepository.save(cardList);
        boardRepository.save(board);
    }
}
