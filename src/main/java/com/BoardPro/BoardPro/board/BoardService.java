package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    public void create(BoardRequest request) {
        Board board = Board.builder()
                .title(request.getTitle())
                .owner(getCurrentUser())
                .build();
        boardRepository.save(board);
    }

    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = Optional.ofNullable((User) auth.getPrincipal());
        return optionalUser.orElseThrow(); //todo obsłużyć wyjątek
    }
}
