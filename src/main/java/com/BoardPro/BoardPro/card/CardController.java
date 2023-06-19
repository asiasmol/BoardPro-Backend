package com.BoardPro.BoardPro.card;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CardRequest request, @RequestParam Long boardId, @RequestParam Long cardListId){
        cardService.add(request, boardId, cardListId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam Long cardId, @RequestParam Long boardId, @RequestParam Long cardListId){
        cardService.remove(cardId, boardId, cardListId);
        return ResponseEntity.ok().build();
    }
}
