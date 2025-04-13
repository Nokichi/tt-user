package ru.jabka.ttuser.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.jabka.ttuser.model.Task;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskClient {

    private final RestTemplate taskServiceRestTemplate;

    public List<Task> getTasksByAssigneeId(final Long id) {
        return List.of(taskServiceRestTemplate.getForObject("/api/v1/task/assignee/" + id, Task[].class));
    }
}