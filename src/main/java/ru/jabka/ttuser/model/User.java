package ru.jabka.ttuser.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record User(
        Long id,
        String username,
        String passwordHash,
        Boolean isDeleted,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}