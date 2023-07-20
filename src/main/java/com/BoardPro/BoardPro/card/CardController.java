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
    public ResponseEntity<CardDTO> add(@RequestBody CardRequest request, @RequestParam Long boardId, @RequestParam Long cardListId){
        return ResponseEntity.ok(cardService.add(request, boardId, cardListId));
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam Long cardId, @RequestParam Long boardId, @RequestParam Long cardListId){
        cardService.remove(cardId, boardId, cardListId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<CardDTO> update(@RequestBody CardRequest request, @RequestParam Long cardId, @RequestParam Long boardId, @RequestParam Long cardListId){
        return ResponseEntity.ok(cardService.update(request, cardId, boardId, cardListId));
    }

    @PatchMapping("/swap")
    public ResponseEntity<CardDTO> swapCardList(@RequestParam Long cardId, @RequestParam Long boardId, @RequestParam Long newCardListId, @RequestParam Long currentCardListId){
        System.out.println(cardId+ "controller cardid");
        return ResponseEntity.ok(cardService.swapCardList(cardId, boardId, newCardListId, currentCardListId));
    }

    @PatchMapping("add-executors")
    public ResponseEntity<CardDTO> update(@RequestParam String userEmail, @RequestParam Long cardId, @RequestParam Long boardId, @RequestParam Long cardListId){
        return ResponseEntity.ok(cardService.adUserToExecutors(userEmail, cardId, boardId, cardListId));
    }
}
