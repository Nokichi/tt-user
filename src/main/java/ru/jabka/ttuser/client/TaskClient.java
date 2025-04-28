package ru.jabka.ttuser.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class TaskClient {

    private final RestTemplate taskServiceRestTemplate;

    public boolean existsActiveTasksByAssignee(final Long id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/api/v1/task/active");
        builder.queryParam("assigneeId", id);
        return taskServiceRestTemplate.getForObject(builder.toUriString(), Boolean.class);
    }
}