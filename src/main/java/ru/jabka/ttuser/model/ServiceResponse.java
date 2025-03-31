package ru.jabka.ttuser.model;

import lombok.Builder;

@Builder
public record ServiceResponse(Boolean success, String message) {
}