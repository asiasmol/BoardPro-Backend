package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.user.UserDTO;
import com.BoardPro.BoardPro.user.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartDTOMapper implements Function<Card, CardDTO> {
    private final UserDTOMapper userDTOMapper;




    @Override
    public CardDTO apply(Card card) {
        Set<UserDTO> executors = (card.getExecutors() != null) ?
                card.getExecutors()
                        .stream()
                        .map(userDTOMapper)
                        .collect(Collectors.toSet()) :
                new HashSet<>();

        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                executors
        );
    }
}
