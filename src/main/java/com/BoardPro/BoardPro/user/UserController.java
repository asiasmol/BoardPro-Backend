package com.BoardPro.BoardPro.user;

import com.BoardPro.BoardPro.cardList.CardListDTO;
import com.BoardPro.BoardPro.cardList.CardListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getUser(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("/all")
    public ResponseEntity<Set<UserDTO>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }


}
