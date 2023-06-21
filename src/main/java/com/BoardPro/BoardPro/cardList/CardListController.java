package com.BoardPro.BoardPro.cardList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cardList")
public class CardListController {

    private final CardListService cardListService;


    @PostMapping
    public ResponseEntity<CardListDTO> add(@RequestBody CardListRequest request, @RequestParam Long boardId){
        cardListService.addListBoard(request, boardId);
        return ResponseEntity.ok(cardListService.addListBoard(request, boardId));
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam Long boardId, @RequestParam Long cardListId){
        cardListService.remove(boardId, cardListId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<CardListDTO> update(@RequestBody CardListRequest request, @RequestParam Long boardId, @RequestParam Long cardListId){
        return ResponseEntity.ok(cardListService.update(request, boardId, cardListId));
    }


}
