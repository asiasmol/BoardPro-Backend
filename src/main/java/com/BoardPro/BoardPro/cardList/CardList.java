package com.BoardPro.BoardPro.cardList;

import com.BoardPro.BoardPro.card.Card;
import com.BoardPro.BoardPro.board.Board;
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
@Table(name="card_lists")
public class CardList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name="boards_id")
    private Board board;

    @OneToMany
    private Set<Card> cards;

}
