package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    private String imagePath;

    @ManyToOne
    private User owner;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<User> users;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CardList> cardLists = new HashSet<>();

    @Builder
    public Board(String title, User owner, String imagePath) {
        this.title = title;
        this.owner = owner;
        this.users = new HashSet<>();
        this.cardLists = new HashSet<>();
        this.imagePath = imagePath;
    }

    void addUser(User user){
        this.users.add(user);
    }

    void removeUser(User user) {
        this.users.remove(user);
    }

    void addCardList(CardList cardList){
        this.cardLists.add(cardList);
    }
}
