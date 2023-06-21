package com.BoardPro.BoardPro.user;

import lombok.EqualsAndHashCode;

public record UserDTO(
           String firstName,
           String lastName,
           String email,
           String role
) {
}
