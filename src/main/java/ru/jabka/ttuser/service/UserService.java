package ru.jabka.ttuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabka.ttuser.client.TaskClient;
import ru.jabka.ttuser.exception.BadRequestException;
import ru.jabka.ttuser.model.ServiceResponse;
import ru.jabka.ttuser.model.User;
import ru.jabka.ttuser.model.UserRequest;
import ru.jabka.ttuser.model.UserResponse;
import ru.jabka.ttuser.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskClient taskClient;

    @Transactional(rollbackFor = Throwable.class)
    public UserResponse create(final UserRequest userRequest) {
        validateUserRequest(userRequest);
        User inserted = userRepository.insert(User.builder()
                .username(userRequest.username())
                .role(userRequest.role())
                .passwordHash(passwordEncoder.encode(userRequest.password()))
                .build());
        return UserResponse.builder()
                .id(inserted.id())
                .role(inserted.role())
                .username(inserted.username())
                .build();
    }

    @Transactional(readOnly = true)
    public UserResponse getById(final Long id) {
        User user = userRepository.getById(id);
        return UserResponse.builder()
                .id(user.id())
                .role(user.role())
                .username(user.username())
                .build();
    }

    @Transactional(readOnly = true)
    public Set<UserResponse> getAllByIds(final Set<Long> ids) {
        List<User> userList = userRepository.getAllByIds(ids);
        return userList.stream()
                .map(x -> UserResponse.builder()
                        .id(x.id())
                        .username(x.username())
                        .role(x.role())
                        .build())
                .collect(Collectors.toSet());
    }

    @Transactional(rollbackFor = Throwable.class)
    public ServiceResponse delete(final Long id) {
        checkActiveUserTasks(id);
        userRepository.delete(id);
        return ServiceResponse.builder()
                .success(true)
                .build();
    }

    private void validateUserRequest(final UserRequest userRequest) {
        ofNullable(userRequest).orElseThrow(() -> new BadRequestException("Введите информацию о пользователе"));
        if (!StringUtils.hasText(userRequest.username())) {
            throw new BadRequestException("Заполните имя пользователя");
        }
        if (!StringUtils.hasText(userRequest.password())) {
            throw new BadRequestException("Заполните пароль пользователя");
        }
        if (userRequest.password().length() < 3) {
            throw new BadRequestException("Минимальная длина пароля: 3 символа");
        }
        ofNullable(userRequest.role()).orElseThrow(() -> new BadRequestException("Заполните роль пользователя"));
    }

    private void checkActiveUserTasks(final Long userId) {
        if (taskClient.existsActiveTasksByAssignee(userId)) {
            throw new BadRequestException(String.format("Присутствуют незавершенные задачи, назначенные на пользователя ID = %s", userId));
        }
    }
}