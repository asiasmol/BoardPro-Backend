package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.board.Board;
import com.BoardPro.BoardPro.board.BoardRepository;
import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.cardList.CardListRepository;
import com.BoardPro.BoardPro.exception.ApiRequestException;
import com.BoardPro.BoardPro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CardService {

    private final BoardRepository boardRepository;
    private final CardListRepository cardListRepository;

    private final CardRepository cardRepository;

    private final CardDTOMapper cardDTOMapper;

    @Transactional
    public CardDTO add(CardRequest request, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        // To do handle exception in proper elegant way

        Card card = Card.builder()
                .title(request.getTitle())
                .cardList(cardList)
                .build();

        cardList.getCards().add(card);
        cardRepository.save(card);
        cardListRepository.save(cardList);
        return cardDTOMapper.apply(card);

    }
    public void remove(Long cardId, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        Card card = cardList.getCards().stream().filter(c -> c.getId() == cardId).findFirst().get();
        cardList.getCards().remove(card);

        cardRepository.delete(card);
        cardListRepository.save(cardList);

    }
    @Transactional
    public CardDTO update(CardRequest request, Long cardId, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        Card card = cardList.getCards().stream().filter(c -> c.getId() == cardId).findFirst().get();
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setExecutors(request.getExecutors());
        card.setCardList(cardListRepository.findById(request.getCardListId()).orElseThrow(() -> new ApiRequestException("Board bo found")));

        cardListRepository.save(cardList);
        return cardDTOMapper.apply(card);
    }

    @Transactional
    public CardDTO adUserToExecutors(String userEmail, Long cardId, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board not found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        Card card = cardList.getCards().stream().filter(c -> c.getId() == cardId).findFirst().get();
        User user = board.getUsers().stream().filter(u -> u.getEmail().equals(userEmail)).findFirst().orElseThrow(() -> new ApiRequestException("User bo found"));
        card.addUserToExecutors(user);
        cardRepository.save(card);
        return cardDTOMapper.apply(card);
    }

}
