package com.BoardPro.BoardPro.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<Set<Board>> gatUserBoards(){
        return ResponseEntity.ok(boardService.getUserBoards());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody BoardRequest request){
        boardService.create(request);
        return  ResponseEntity.ok().build();
    }

}
