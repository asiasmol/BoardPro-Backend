package com.BoardPro.BoardPro.board;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.cardList.CardListDTO;
import com.BoardPro.BoardPro.user.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        // Prepare test data if needed
    }

    @Test
    public void testGetUserBoards() throws Exception {
        Set<BoardDTO> boards = Collections.emptySet();
        when(boardService.getUserBoards()).thenReturn(boards);

        mockMvc.perform(get("/api/board/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boards)));
    }

    @Test
    public void testCreate() throws Exception {
        Set<CardList> cardLists = Collections.emptySet();
        BoardRequest request = new BoardRequest("Test Board", cardLists, "path/to/image");
        request.setTitle("Test Board");
        request.setImagePath("path/to/image");

        UserDTO owner = new UserDTO("test", "test", "test@op.pl", "user");
        Set<CardListDTO> cardListsDTO = Collections.emptySet();
        Set<UserDTO> users = Collections.emptySet();
        BoardDTO response = new BoardDTO(1L, "Test Board", owner, users, cardListsDTO, "path/to/image");

        when(boardService.create(request)).thenReturn(response);

        mockMvc.perform(post("/api/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void testRemove() throws Exception {
        Long boardId = 1L;

        mockMvc.perform(delete("/api/board")
                        .param("boardId", boardId.toString()))
                .andExpect(status().isOk());

        verify(boardService, times(1)).remove(boardId);
    }
}