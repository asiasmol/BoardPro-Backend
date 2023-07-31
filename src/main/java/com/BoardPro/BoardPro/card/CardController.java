package com.BoardPro.BoardPro.card;

import com.BoardPro.BoardPro.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    public ResponseEntity<Set<CardDTO>> swap(@RequestBody Set<CardRequest> requests, @RequestParam Long boardId) {
        return ResponseEntity.ok(cardService.swapCards(requests, boardId));
    }

    @PatchMapping("add-executors")
    public ResponseEntity<UserDTO> update(@RequestParam Long cardId, @RequestParam Long boardId, @RequestParam Long cardListId, @RequestParam String userEmail){
        return ResponseEntity.ok(cardService.adUserToExecutors(userEmail, cardId, boardId, cardListId));
    }

}
