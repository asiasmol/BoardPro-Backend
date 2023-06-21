package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.cardList.CardList;
import com.BoardPro.BoardPro.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @OneToMany
    private Set<User> executors;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private CardList cardList;

    void addUserToExecutors(User user){
        this.executors.add(user);
    }
}