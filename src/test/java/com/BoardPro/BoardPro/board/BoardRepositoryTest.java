package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.user.User;
import com.BoardPro.BoardPro.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    void shouldFindAllByUserEmail() {
        // given
        String email = "jk@op.pl";
        User user = new User();
        user.setEmail(email);

        Board testBoard1 = new Board();
        testBoard1.setTitle("title1");
        testBoard1.setUsers(Set.of(user));

        Board testBoard2 = new Board();
        testBoard2.setTitle("title2");
        testBoard2.setUsers(Set.of(user));

        Set<Board> givenBoards = Set.of(testBoard1, testBoard2);
        userRepository.save(user);
        boardRepository.save(testBoard1);
        boardRepository.save(testBoard2);

        // when
        Set<Board> expectedBoards = boardRepository.findAllByUserEmail("jk@op.pl");

        // then
        assertEquals(givenBoards, expectedBoards);
    }
}