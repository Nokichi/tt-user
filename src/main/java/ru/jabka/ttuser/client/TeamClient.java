package ru.jabka.ttuser.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class TeamClient {

    private final RestTemplate teamServiceRestTemplate;

    public boolean existsByOwnerId(final Long ownerId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/api/v1/team/exists");
        builder.queryParam("ownerId", ownerId);
        return teamServiceRestTemplate.getForObject(builder.toUriString(), Boolean.class);
    }
}