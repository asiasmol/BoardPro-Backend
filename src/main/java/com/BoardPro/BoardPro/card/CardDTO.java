package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.user.UserDTO;


import java.util.Set;

public record CardDTO(
        Long id,
        String title,
        String description,
        Set<UserDTO>executors,
        Long cardListId
) {
}
