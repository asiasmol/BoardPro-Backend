package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.board.Board;
import com.BoardPro.BoardPro.board.BoardRepository;
import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.cardList.CardListRepository;
import com.BoardPro.BoardPro.exception.ApiRequestException;
import com.BoardPro.BoardPro.user.User;
import com.BoardPro.BoardPro.user.UserDTO;
import com.BoardPro.BoardPro.user.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CardService {

    private final BoardRepository boardRepository;
    private final CardListRepository cardListRepository;
    private final CardRepository cardRepository;
    private final CardDTOMapper cardDTOMapper;
    private final UserDTOMapper userDTOMapper;

    @Transactional
    public CardDTO addCardToCardList(CardRequest request, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        int orderNumber = cardList.getCards().size() + 1;

        Card card = Card.builder()
                .title(request.getTitle())
                .cardList(cardList)
                .orderNumber(orderNumber)
                .build();

        cardList.getCards().add(card);
        cardRepository.save(card);
        cardListRepository.save(cardList);
        return cardDTOMapper.apply(card);

    }
    public void deleteCard(Long cardId, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        Card card = cardList.getCards().stream().filter(c -> c.getId() == cardId).findFirst().get();
        cardList.getCards().remove(card);

        cardRepository.delete(card);
        cardListRepository.save(cardList);

    }
    @Transactional
    public CardDTO updateCard(CardRequest request, Long cardId, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board no found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        Card card = cardList.getCards().stream().filter(c -> c.getId() == cardId).findFirst().get();
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setExecutors(request.getExecutors());
        card.setCardList(cardListRepository.findById(request.getCardListId()).orElseThrow(() -> new ApiRequestException("CardList not found")));
        cardListRepository.save(cardList);
        return cardDTOMapper.apply(card);
    }

    @Transactional
    public UserDTO addUserToExecutors(String userEmail, Long cardId, Long boardId, Long cardListId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board not found"));
        CardList cardList = board.getCardLists().stream().filter(c -> c.getId() == cardListId).findAny().get();
        Card card = cardList.getCards().stream().filter(c -> c.getId() == cardId).findFirst().get();
        User user = board.getUsers().stream().filter(u -> u.getEmail().equals(userEmail)).findFirst().orElseThrow(() -> new ApiRequestException("User bo found"));
        card.addUserToExecutors(user);
        cardRepository.save(card);
        return userDTOMapper.apply(user);
    }

    @Transactional
    public Set<CardDTO> swapCards(Set<CardRequest> requests, Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board not found"));
        return requests.stream().map(request -> {
            Card card = cardRepository.findById(request.getId()).orElseThrow(() -> new ApiRequestException("Card not found"));
            Long newCardListId = request.getCardListId();
            CardList updatedCardList = board.getCardLists().stream().filter(c -> c.getId() == newCardListId).findAny().orElseThrow(() -> new ApiRequestException("CardList not found"));
            card.setOrderNumber(request.getOrderNumber());
            card.setCardList(updatedCardList);
            cardRepository.save(card);
            return cardDTOMapper.apply(card);
        }).collect(Collectors.toSet());
    }

}
