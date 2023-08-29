package com.BoardPro.BoardPro.cardList;

import com.BoardPro.BoardPro.board.Board;
import com.BoardPro.BoardPro.board.BoardRepository;
import com.BoardPro.BoardPro.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardListService {

    private final CardListRepository cardListRepository;
    private final BoardRepository boardRepository;

    private final CardListDTOMapper cardListDTOMapper;

    public CardListDTO addListToBoard(CardListRequest request, Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        // To do handle exception in proper elegant way

        CardList cardList = CardList.builder()
                .title(request.getTitle())
                .board(board)
                .build();

        board.getCardLists().add(cardList);
        cardListRepository.save(cardList);
        boardRepository.save(board);
        return cardListDTOMapper.apply(cardList);
    }

    public void deleteCardList(Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        board.getCardLists().remove(cardList);

        cardListRepository.delete(cardList);
        boardRepository.save(board);
    }

    @Transactional
    public CardListDTO update(CardListRequest request, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        cardList.setTitle(request.getTitle());

        cardListRepository.save(cardList);
        return cardListDTOMapper.apply(cardList);
    }
}
