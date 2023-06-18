package com.BoardPro.BoardPro.cardList;

import com.BoardPro.BoardPro.board.Board;
import com.BoardPro.BoardPro.board.BoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cardList")
public class CardListController {

    private final CardListService cardListService;


    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CardListRequest request, @RequestParam Long boardId){
        cardListService.addListBoard(request, boardId);
        return ResponseEntity.ok().build();
    }
}
