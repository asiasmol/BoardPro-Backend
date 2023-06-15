package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @OneToOne
    private User owner;
    @OneToMany
    private Set<User> users;
    @OneToMany
    private Set<CardList> cardLists;
}
