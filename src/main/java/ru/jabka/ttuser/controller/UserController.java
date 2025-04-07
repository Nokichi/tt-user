package ru.jabka.ttuser.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.jabka.ttuser.model.UserRequest;
import ru.jabka.ttuser.model.UserResponse;
import ru.jabka.ttuser.service.UserService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создание нового пользователя")
    public UserResponse create(@RequestBody final UserRequest userRequest) {
        return userService.create(userRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по ID")
    public UserResponse getById(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Получение списка пользователей по множеству ID")
    public Set<UserResponse> getListById(@RequestParam final Set<Long> ids) {
        return userService.getAllByIds(ids);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя по ID")
    public ServiceResponse delete(@PathVariable final Long id) {
        return userService.delete(id);
    }
}