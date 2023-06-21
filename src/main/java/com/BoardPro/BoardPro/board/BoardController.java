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
    public ResponseEntity<Set<BoardDTO>> gatUserBoards(){
        return ResponseEntity.ok(boardService.getUserBoards());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody BoardRequest request){
        boardService.create(request);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam Long boardId){
        boardService.remove(boardId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody BoardRequest request, @RequestParam Long boardId){
        boardService.update(request, boardId);
        return ResponseEntity.ok().build();
    }


}
