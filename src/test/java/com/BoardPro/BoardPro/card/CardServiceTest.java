package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.board.Board;
import com.BoardPro.BoardPro.board.BoardRepository;
import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.cardList.CardListRepository;
import com.BoardPro.BoardPro.exception.ApiRequestException;
import com.BoardPro.BoardPro.user.User;
import com.BoardPro.BoardPro.user.UserDTOMapper;
import com.BoardPro.BoardPro.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardListRepository cardListRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardDTOMapper cardDTOMapper;
    @Mock
    private UserDTOMapper userDTOMapper;
    private Board board;
    private CardList cardList;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        board = new Board();
        cardList = new CardList();
        cardList.setId(1L);
        board.setId(1L);
        board.setCardLists(Set.of(cardList));
        cardService = new CardService(boardRepository, cardListRepository, cardRepository, cardDTOMapper, userDTOMapper);
    }

    @Test
    void canAddCardToCardList() {
        // given
        CardRequest request = new CardRequest();
        request.setTitle("testTitle");

        Set<Card> cards = new HashSet<>();
        cardList.setCards(cards);

        int orderNumber = 1;

        Card expectedCard = Card.builder()
                .title(request.getTitle())
                .cardList(cardList)
                .orderNumber(orderNumber)
                .description(null)
                .executors(null)
                .build();

        CardDTO expectedDTO = new CardDTO(expectedCard.getId(), expectedCard.getTitle(), null, expectedCard.getOrderNumber(), null, null);

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(cardDTOMapper.apply(any(Card.class))).thenReturn(expectedDTO);
        CardDTO result = cardService.addCardToCardList(request, board.getId(), cardList.getId());

        // then
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);

        verify(cardRepository).save(cardArgumentCaptor.capture());
        assertEquals(expectedDTO, result);
    }

    @Test
    void canNotAddCardToCardList() {
        // given
        CardRequest request = new CardRequest();
        request.setTitle("testTitle");

        Long nonExistingId = 2L;

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardService.addCardToCardList(request, nonExistingId, cardList.getId());
        });
    }
    @Test
    void canDeleteCard() {
        // given
        Card card = new Card();
        card.setId(1L);
        Set<Card> cards = new HashSet<>();
        cards.add(card);
        cardList.setCards(cards);

        Set<Card> expected = Set.of();

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        cardService.deleteCard(card.getId(), cardList.getId(), board.getId());

        Set<Card> result = cardList.getCards();

        // then
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);

        verify(cardRepository).delete(cardArgumentCaptor.capture());
        assertEquals(expected, result);
    }

    @Test
    void canNotDeleteCard() {
        // given
        Card card = new Card();
        card.setId(1L);

        Long nonExistingId = 2L;

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardService.deleteCard(card.getId(), cardList.getId(), nonExistingId);
        });
    }
    @Test
    void updateCard() {
        // given
        CardRequest request = new CardRequest();
        request.setTitle("testTitle");
        request.setCardListId(cardList.getId());

        Card card = new Card();
        card.setId(1L);

        Set<Card> cards = new HashSet<>();
        cards.add(card);
        cardList.setCards(cards);

        CardDTO expectedDTO = new CardDTO(card.getId(), "testTitle", null, null, null, cardList.getId());

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(cardDTOMapper.apply(any(Card.class))).thenReturn(expectedDTO);
        when(cardListRepository.findById(cardList.getId())).thenReturn(Optional.of(cardList));

        CardDTO result = cardService.updateCard(request, card.getId(), cardList.getId(), board.getId());

        // then
        assertEquals(expectedDTO, result);
    }

    @Test
    void canNotUpdateCard() {
        // given
        CardRequest request = new CardRequest();
        request.setTitle("testTitle");

        Card card = new Card();
        card.setId(1L);

        Long nonExistingId = 2L;

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardService.updateCard(request, card.getId(), cardList.getId(), nonExistingId);
        });
    }
    @Test
    void addUser() {
        // given
        User user = new User();
        String email = "test@gmail.com";
        user.setEmail(email);

        Card card = new Card();
        card.setId(1L);

        Set<Card> cards = new HashSet<>();
        cards.add(card);
        cardList.setCards(cards);
        board.setUsers(Set.of(user));
        Set<User> executors = new HashSet<>();
        card.setExecutors(executors);

        Set<User> expected = Set.of(user);

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        cardService.addUserToExecutors(email, card.getId(), cardList.getId(), board.getId());

        Set<User> result = card.getExecutors();

        // then
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardArgumentCaptor.capture());

        assertEquals(expected, result);
    }
    @Test
    void canNotAddUserBecauseBoardDoNotExist() {
        // given
        User user = new User();
        String email = "test@gmail.com";
        user.setEmail(email);

        Card card = new Card();
        card.setId(1L);

        Long nonExistingId = 2L;

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardService.addUserToExecutors(email, card.getId(), cardList.getId(), nonExistingId);
        });
    }


    @Test
    void swapCards () {
        // given
        CardRequest request = new CardRequest();
        request.setId(1L);
        request.setCardListId(1L);
        request.setOrderNumber(1);
        Set<CardRequest> requests = Set.of(request);

        Card card = new Card();
        card.setId(1L);

        Set<Card> cards = new HashSet<>();
        cards.add(card);
        cardList.setCards(cards);

        CardDTO expectedDTO = new CardDTO(1L, null, null, 1, null, 1L);
        Set<CardDTO> expected = Set.of(expectedDTO);

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(cardRepository.findById(request.getId())).thenReturn(Optional.of(card));
        when(cardDTOMapper.apply(any(Card.class))).thenReturn(expectedDTO);

        Set<CardDTO> result = cardService.swapCards(requests, board.getId());

        // then
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardArgumentCaptor.capture());

        assertEquals(expected, result);
    }

    @Test
    void canNotSwapCardsBecauseBoardDoNotExist() {
        // given
        CardRequest request = new CardRequest();
        Set<CardRequest> requests = Set.of(request);

        Long nonExistingId = 2L;

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardService.swapCards(requests, nonExistingId);
        });
    }

    @Test
    void canNotSwapCardsBecauseCardDoNotExist() {
        // given
        CardRequest request = new CardRequest();
        request.setId(2L);
        request.setCardListId(1L);
        request.setOrderNumber(1);
        Set<CardRequest> requests = Set.of(request);

        Card card = new Card();
        card.setId(1L);

        Set<Card> cards = new HashSet<>();
        cards.add(card);
        cardList.setCards(cards);

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardService.swapCards(requests, board.getId());
        });
    }
    @Test
    void canNotSwapCardsToNonExistingCardList() {
        // given
        CardRequest request = new CardRequest();
        request.setId(1L);
        request.setCardListId(2L);
        request.setOrderNumber(1);
        Set<CardRequest> requests = Set.of(request);

        Card card = new Card();
        card.setId(1L);

        Set<Card> cards = new HashSet<>();
        cards.add(card);
        cardList.setCards(cards);

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(cardRepository.findById(request.getId())).thenReturn(Optional.of(card));

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardService.swapCards(requests, board.getId());
        });
    }
}