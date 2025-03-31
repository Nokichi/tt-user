package ru.jabka.ttuser.model;

public record UserRequest(
        String username,
        String password
) {
}