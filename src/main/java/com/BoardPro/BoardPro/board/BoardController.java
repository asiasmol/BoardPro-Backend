package com.BoardPro.BoardPro.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;


    @PostMapping
    public ResponseEntity<Void> create(@RequestBody BoardRequest request){
        boardService.create(request);
        return  ResponseEntity.ok().build();
    }

}
