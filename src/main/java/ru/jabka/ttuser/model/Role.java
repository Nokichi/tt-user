package ru.jabka.ttuser.model;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Role {

    MANAGER(1L),
    USER(2L);

    private final Long id;

    Role(Long id) {
        this.id = id;
    }

    public static Role byId(Long id) {
        return Stream.of(Role.values())
                .filter(x -> id.equals(x.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(String.format("Роль с id = %d не найден", id)));
    }
}