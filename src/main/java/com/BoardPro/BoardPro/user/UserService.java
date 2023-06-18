package com.BoardPro.BoardPro.user;

import com.BoardPro.BoardPro.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
