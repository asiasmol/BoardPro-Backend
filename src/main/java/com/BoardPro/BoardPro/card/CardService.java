package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.board.Board;
import com.BoardPro.BoardPro.board.BoardRepository;
import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.cardList.CardListRepository;
import com.BoardPro.BoardPro.cardList.CardListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final BoardRepository boardRepository;
    private final CardListRepository cardListRepository;

    private final CardRepository cardRepository;

    public void add(CardRequest request, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new RuntimeException("Board bo found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        // To do handle exception in proper elegant way

        Card card = Card.builder()
                .title(request.getTitle())
                .cardList(cardList)
                .build();


        cardList.getCards().add(card);
        cardRepository.save(card);
        cardListRepository.save(cardList);

    }
}
