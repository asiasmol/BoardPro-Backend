package com.BoardPro.BoardPro.board;

import com.BoardPro.BoardPro.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    //zwraca boardy należące do usera
    @GetMapping("/all")
    public ResponseEntity<Set<BoardDTO>> gatUserBoards(){
        return ResponseEntity.ok(boardService.getUserBoards());
    }

    @PostMapping
    public ResponseEntity<BoardDTO> create(@RequestBody BoardRequest request){
        return  ResponseEntity.ok(boardService.create(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam Long boardId){
        boardService.remove(boardId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<BoardDTO> update(@RequestBody BoardRequest request, @RequestParam Long boardId){
        return  ResponseEntity.ok(boardService.update(request, boardId));
    }

    @PatchMapping("/add-user")
    public ResponseEntity<UserDTO> adUserToBoard(@RequestParam String userEmail, @RequestParam Long boardId){
        return  ResponseEntity.ok(boardService.addUserToBoard(userEmail, boardId));
    }

    //zwraca userów należących do boarda
    @GetMapping("/users")
    public ResponseEntity<Set<UserDTO>> getBoardUsers(@RequestParam Long boardId){
        return ResponseEntity.ok(boardService.getBoardUser(boardId));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long boardId){
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @DeleteMapping("/remove-user")
    public ResponseEntity<UserDTO> removeUser(@RequestParam String userEmail, @RequestParam Long boardId) {
        return ResponseEntity.ok(boardService.removeUser(userEmail, boardId));
    }


}
