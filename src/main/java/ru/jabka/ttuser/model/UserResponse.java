package ru.jabka.ttuser.model;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        Role role,
        String username
) {
}