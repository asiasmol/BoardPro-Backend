package com.BoardPro.BoardPro.user;

import com.BoardPro.BoardPro.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    public UserDTO getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return userDTOMapper.apply(userRepository.findByEmail(user.getEmail()).orElseThrow(()-> new ApiRequestException("Board bo found")));
    }

    public Set<UserDTO> getUsers(){
        Set<User> users = userRepository.getAll();
        return users.stream().map(userDTOMapper::apply).collect(Collectors.toSet());
    }
}
