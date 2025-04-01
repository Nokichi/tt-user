package ru.jabka.ttuser.model;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String username
) {
}