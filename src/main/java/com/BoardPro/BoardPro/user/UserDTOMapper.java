package com.BoardPro.BoardPro.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {


    @Override
    public UserDTO apply(User user) {
        return new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get());
    }
}
