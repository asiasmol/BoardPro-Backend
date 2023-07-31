package com.BoardPro.BoardPro.card;


import com.BoardPro.BoardPro.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
    private Long id;
    private String title;
    private String description;
    private Set<User> executors;
    private Long cardListId;
    private Integer orderNumber;
}
