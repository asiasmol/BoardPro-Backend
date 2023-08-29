package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.exception.ApiRequestException;
import com.BoardPro.BoardPro.user.User;
import com.BoardPro.BoardPro.user.UserDTO;
import com.BoardPro.BoardPro.user.UserDTOMapper;
import com.BoardPro.BoardPro.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardDTOMapper boardDTOMapper;
    private final UserDTOMapper userDTOMapper;



    @Transactional
    public BoardDTO create(BoardRequest request){
        User user = getCurrentUser();
        Board board = Board.builder()
                .title(request.getTitle())
                .owner(user)
                .imagePath(request.getImagePath())
                .build();
        board.getUsers().add(user);
        user.addBoard(board);
        boardRepository.save(board);
        return boardDTOMapper.apply(board);
    }

    public void remove(Long boardId) {
        User user = getCurrentUser();
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        user.getBoards().remove(board);

        userRepository.save(user);
        boardRepository.delete(board);

    }

    @Transactional
    public BoardDTO update(BoardRequest request, Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board bo found"));
        board.setTitle(request.getTitle());
        board.setCardLists(request.getCardLists());
        boardRepository.save(board);
        return boardDTOMapper.apply(board);
    }

    User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return userRepository.findByEmail(user.getEmail()).orElseThrow(()-> new ApiRequestException("User not found"));
    }

    public Set<BoardDTO> getUserBoards() {
        return boardRepository.findAllByUserEmail(getCurrentUser().getEmail())
                .stream()
                .map(boardDTOMapper).collect(Collectors.toSet());

    }

    @Transactional
    public UserDTO addUserToBoard(String userEmail, Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board not found"));
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        User user = optionalUser.orElseThrow(() -> new ApiRequestException("User not found"));
        board.addUser(user);
        boardRepository.save(board);
        return userDTOMapper.apply(user);
    }


    public Set<UserDTO> getBoardUser(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board not found"));
        return board.getUsers().stream().map(userDTOMapper).collect(Collectors.toSet());
    }

    public BoardDTO getBoard(Long boardId) {
        Board board = getCurrentUser().getBoards().stream().filter(b -> b.getId()==boardId).findAny().orElseThrow(() -> new ApiRequestException("Board not found"));
        return boardDTOMapper.apply(board);
    }


    public UserDTO removeUser(String userEmail, Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new ApiRequestException("Board not found"));
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        User user = optionalUser.orElseThrow(() -> new ApiRequestException("User not found"));

        board.removeUser(user);
        boardRepository.save(board);
        return userDTOMapper.apply(user);
    }
}