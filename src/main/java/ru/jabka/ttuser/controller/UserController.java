package ru.jabka.ttuser.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jabka.ttuser.model.ServiceResponse;
import ru.jabka.ttuser.model.User;
import ru.jabka.ttuser.model.UserRequest;
import ru.jabka.ttuser.service.UserService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody final UserRequest userRequest) {
        return userService.create(userRequest);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public Set<User> getByUsername(@RequestParam final String username) {
        return userService.getAllByUsername(username);
    }

    @DeleteMapping("/{id}")
    public ServiceResponse delete(@PathVariable final Long id) {
        return userService.delete(id);
    }
}