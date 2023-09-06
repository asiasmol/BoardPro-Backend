package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.exception.ApiRequestException;
import com.BoardPro.BoardPro.user.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardDTOMapper boardDTOMapper;
    @Mock
    private UserDTOMapper userDTOMapper;
    @Mock
    SecurityContext securityContext;
    @Mock
    Authentication authentication;
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardRepository, userRepository, boardDTOMapper, userDTOMapper);
        SecurityContextHolder.setContext(securityContext);
    }
    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
    @Test
    void canCreate() {
        // given
        BoardRequest request = new BoardRequest("test", null, "test");
        User user = new User();
        String email = "test@email.com";
        user.setEmail(email);
        user.setRole(Role.USER);

        UserDTO userDTO = new UserDTO(null, null, email, "USER");

        Board expectedBoard = Board.builder()
                .title(request.getTitle())
                .owner(user)
                .imagePath(request.getImagePath())
                .build();

        BoardDTO expectedDTO = new BoardDTO(expectedBoard.getId(),
                expectedBoard.getTitle(),
                userDTO,
                Set.of(userDTO),
                null,
                expectedBoard.getImagePath());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(boardRepository.save(any(Board.class))).thenReturn(expectedBoard);
        when(boardDTOMapper.apply(any(Board.class))).thenReturn(expectedDTO);

        // when
        BoardDTO result = boardService.create(request);

        // then
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);

        verify(boardRepository).save(boardArgumentCaptor.capture());
        assertEquals(expectedDTO, result);
    }

    @Test
    void canGetUserBoards() {
        // given
        String email = "test@email.com";
        User user = new User();
        user.setEmail(email);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        boardService.getUserBoards();

        // then
        verify(boardRepository).findAllByUserEmail(email);
    }

    @Test
    void canDeleteBoard() {
        // given
        Board board = new Board();
        board.setId(1L);
        String email = "test@email.com";
        User user = new User();
        user.setEmail(email);
        user.addBoard(board);
        Set<Board> expectedBoards = Set.of();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when
        boardService.remove(board.getId());
        Set<Board> result = user.getBoards();

        // then
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).delete(boardArgumentCaptor.capture());
        assertEquals(expectedBoards, result);
    }

    @Test
    void canUpdate() {
        // given
        BoardRequest request = new BoardRequest();
        request.setTitle("updatedTitle");

        Board board = new Board();
        board.setId(1L);

        BoardDTO expectedDTO = new BoardDTO(board.getId(),
                request.getTitle(),
                null,
                null,
                null,
                null);

        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        when(boardDTOMapper.apply(any(Board.class))).thenReturn(expectedDTO);

        // when
        BoardDTO result = boardService.update(request, board.getId());

        // then
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardArgumentCaptor.capture());
        assertEquals(expectedDTO, result);
    }

    @Test
    void canNotAddUser() {
        // given
        Board board = new Board();
        Set<User> users = new HashSet<>();
        board.setId(1L);
        board.setUsers(users);

        String nonExistingEmail = "test@email.com";

        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when-then
        assertThrows(ApiRequestException.class, () -> {
            boardService.addUserToBoard(nonExistingEmail, board.getId());
        });
    }

    @Test
    void canAddUser() {
        // given
        Board board = new Board();
        Set<User> users = new HashSet<>();
        board.setId(1L);
        board.setUsers(users);

        User user = new User();
        String email = "test@email.com";
        user.setEmail(email);
        user.setRole(Role.USER);

        Set<User> expectedUserSet = Set.of(user);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // when
        boardService.addUserToBoard(email, board.getId());
        Set<User> result = board.getUsers();

        // then
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardArgumentCaptor.capture());
        assertEquals(expectedUserSet, result);
    }

    @Test
    void canGetBoardUsers() {
        // given
        User user = new User();
        String email = "test@email.com";
        user.setEmail(email);
        user.setRole(Role.USER);

        UserDTO userDTO = new UserDTO(null, null, email, "USER");
        Set<UserDTO> expectedUserDTOSet = Set.of(userDTO);

        Board board = new Board();
        Set<User> users = Set.of(user);
        board.setId(1L);
        board.setUsers(users);

        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(userDTOMapper.apply(any(User.class))).thenReturn(userDTO);

        // when
        Set<UserDTO> result = boardService.getBoardUser(board.getId());

        // then
        assertEquals(expectedUserDTOSet, result);
    }

    @Test
    void canNotGetBoard() {
        // given
        Long nonExistentBoardId = 1L;
        User user = new User();
        user.setBoards(new HashSet<>());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when-then
        assertThrows(ApiRequestException.class, () -> {
            boardService.getBoard(nonExistentBoardId);
        });
    }


    @Test
    void canGetBoard() {
        // given
        Board board = new Board();
        board.setId(1L);
        Set<Board> boards = Set.of(board);

        User user = new User();
        user.setBoards(boards);

        BoardDTO expectedDTO = new BoardDTO(board.getId(), null, null, null, null, null);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(boardDTOMapper.apply(any(Board.class))).thenReturn(expectedDTO);

        // when
        BoardDTO result = boardService.getBoard(board.getId());

        // then
        assertEquals(expectedDTO, result);
    }

    @Test
    void canNotRemoveUser() {
        // given
        Board board = new Board();
        board.setId(1L);
        Set<User> users = new HashSet<>();
        board.setUsers(users);

        String nonExistingEmail = "test@email.com";

        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when-then
        assertThrows(ApiRequestException.class, () -> {
            boardService.removeUser(nonExistingEmail, board.getId());
        });
    }

    @Test
    void canRemoveUser() {
        // given
        User user = new User();
        String email = "test@email.com";
        user.setEmail(email);
        user.setRole(Role.USER);

        Board board = new Board();
        board.setId(1L);
        Set<User> users = new HashSet<>();
        users.add(user);
        board.setUsers(users);
        Set<User> expectedUserSet = Set.of();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when
        boardService.removeUser(email, board.getId());

        Set<User> result = board.getUsers();

        // then
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardArgumentCaptor.capture());
        assertEquals(expectedUserSet, result);
    }
}