package com.BoardPro.BoardPro.cardList;

import com.BoardPro.BoardPro.board.Board;
import com.BoardPro.BoardPro.board.BoardRepository;
import com.BoardPro.BoardPro.exception.ApiRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardListServiceTest {
    @Mock
    private CardListRepository cardListRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CardListDTOMapper cardListDTOMapper;
    private CardListService cardListService;
    private Board board;
    @BeforeEach
    void setUp() {
        board = new Board();
        board.setId(1L);
        Set<CardList> cardLists = new HashSet<>();
        board.setCardLists(cardLists);
        cardListService = new CardListService(cardListRepository, boardRepository, cardListDTOMapper);
    }

    @Test
    void canAddListToBoard() {
        // given
        CardListRequest request = new CardListRequest();
        request.setTitle("testTitle");

        CardList expectedCardList = CardList.builder()
                .title(request.getTitle())
                .board(board)
                .cards(null)
                .build();

        CardListDTO expectedDTO = new CardListDTO(expectedCardList.getId(), expectedCardList.getTitle(), null);

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(cardListDTOMapper.apply(any(CardList.class))).thenReturn(expectedDTO);

        CardListDTO result = cardListService.addListToBoard(request, board.getId());

        // then
        ArgumentCaptor<CardList> cardListArgumentCaptor = ArgumentCaptor.forClass(CardList.class);

        verify(cardListRepository).save(cardListArgumentCaptor.capture());
        assertEquals(expectedDTO, result);
    }

    @Test
    void canNotAddListToBoardWhichNotExist() {
        // given
        CardListRequest request = new CardListRequest();
        request.setTitle("testTitle");

        Long nonExistingId = 2L;

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardListService.addListToBoard(request, nonExistingId);
        });
    }

    @Test
    void canDeleteList() {
        // given
        CardList cardList = new CardList();
        cardList.setId(1L);

        board.getCardLists().add(cardList);

        Set<CardList> expected = new HashSet<>();

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        cardListService.deleteCardList(cardList.getId(), board.getId());

        Set<CardList> result = board.getCardLists();

        // then
        ArgumentCaptor<CardList> cardListArgumentCaptor = ArgumentCaptor.forClass(CardList.class);

        verify(cardListRepository).delete(cardListArgumentCaptor.capture());
        assertEquals(expected, result);
    }

    @Test
    void canNotDeleteListBecauseBoardDoNotExist() {
        // given
        CardList cardList = new CardList();
        cardList.setId(1L);

        Long nonExistingId = 2L;

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardListService.deleteCardList(cardList.getId(), nonExistingId);
        });
    }

    @Test
    void canUpdateList() {
        // given
        CardListRequest request = new CardListRequest();
        request.setTitle("testTitle");

        CardList cardList = new CardList();
        cardList.setId(1L);

        board.getCardLists().add(cardList);

        CardListDTO expectedDTO = new CardListDTO(1L, request.getTitle(), null);

        // when
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(cardListDTOMapper.apply(any(CardList.class))).thenReturn(expectedDTO);

        CardListDTO result = cardListService.update(request, board.getId(), cardList.getId());

        // then
        ArgumentCaptor<CardList> cardListArgumentCaptor = ArgumentCaptor.forClass(CardList.class);

        verify(cardListRepository).save(cardListArgumentCaptor.capture());
        assertEquals(expectedDTO, result);
    }

    @Test
    void canNotUpdateListBecauseBoardDoNotExist() {
        // given
        CardListRequest request = new CardListRequest();
        request.setTitle("testTitle");

        CardList cardList = new CardList();
        cardList.setId(1L);

        Long nonExistingId = 2L;

        // then
        assertThrows(ApiRequestException.class, () -> {
            cardListService.update(request, cardList.getId(), nonExistingId);
        });
    }
}

