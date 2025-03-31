package ru.jabka.ttuser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabka.ttuser.model.ServiceResponse;
import ru.jabka.ttuser.model.User;
import ru.jabka.ttuser.model.UserRequest;
import ru.jabka.ttuser.repository.UserRepository;

import java.util.Set;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Throwable.class)
    public User create(final UserRequest userRequest) {
        validateUserRequest(userRequest);
        return userRepository.insert(User.builder()
                .username(userRequest.username())
                .passwordHash(passwordEncoder.encode(userRequest.password()))
                .build());
    }

    @Transactional(readOnly = true)
    public User getById(final Long id) {
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public Set<User> getAllByUsername(final String username) {
        return Set.copyOf(userRepository.getAllByName("%" + username + "%"));
    }

    @Transactional(rollbackFor = Throwable.class)
    public ServiceResponse delete(final Long id) {
        userRepository.delete(id);
        return ServiceResponse.builder()
                .success(true)
                .build();
    }

    private void validateUserRequest(final UserRequest userRequest) {
        ofNullable(userRequest).orElseThrow(() -> new RuntimeException("Введите информацию о пользователе"));
        if (!StringUtils.hasText(userRequest.username())) {
            throw new RuntimeException("Заполните имя пользователя");
        }
        if (!StringUtils.hasText(userRequest.password())) {
            throw new RuntimeException("Заполните пароль пользователя");
        }
    }
}