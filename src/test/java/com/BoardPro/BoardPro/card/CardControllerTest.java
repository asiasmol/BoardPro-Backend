package com.BoardPro.BoardPro.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CardControllerTest {
    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldAddCardToCardList() throws Exception {
        Long boardId = 1L;
        Long cardListId = 1L;
        CardRequest request = new CardRequest();
        // populate request object here

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .param("boardId", boardId.toString())
                        .param("cardListId", cardListId.toString()))
                .andExpect(status().isOk());

        Mockito.verify(cardService, times(1)).addCardToCardList(request, boardId, cardListId);
    }
}