package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.user.User;
import com.BoardPro.BoardPro.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;



    @Transactional
    public void create(BoardRequest request){
        User user = getCurrentUser();
        Board board = Board.builder()
                .title(request.getTitle())
                .owner(user)
                .build();
        board.getUsers().add(user);
        user.getBoards().add(board);
        boardRepository.save(board);
        userRepository.save(user);
    }

    public void remove(Long boardId) {
        User user = getCurrentUser();
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new RuntimeException("Board bo found"));
        user.getBoards().remove(board);

        userRepository.save(user);
        boardRepository.delete(board);

    }

    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = Optional.ofNullable((User) auth.getPrincipal());
        return optionalUser.orElseThrow(); //todo obsłużyć wyjątek
    }

    public Set<Board> getUserBoards() {
        return boardRepository.findAllByUserEmail(getCurrentUser().getEmail());
    }


}
