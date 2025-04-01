package ru.jabka.ttuser.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.jabka.ttuser.model.ServiceResponse;
import ru.jabka.ttuser.model.User;
import ru.jabka.ttuser.model.UserRequest;
import ru.jabka.ttuser.model.UserResponse;
import ru.jabka.ttuser.repository.UserRepository;

import java.util.Collections;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void create_success() {
        final UserRequest userRequest = new UserRequest("Test", "test123Pass&6");
        final User user = User.builder()
                .username(userRequest.username())
                .passwordHash(passwordEncoder.encode(userRequest.password()))
                .build();
        Mockito.when(userRepository.insert(user)).thenReturn(user);
        UserResponse expected = UserResponse.builder()
                .id(user.id())
                .username(user.username())
                .build();
        UserResponse result = userService.create(userRequest);
        Assertions.assertEquals(expected, result);
        Mockito.verify(userRepository).insert(user);
    }

    @Test
    void getById_success() {
        final User user = getValidUser();
        Mockito.when(userRepository.getById(user.id())).thenReturn(user);
        UserResponse expected = UserResponse.builder()
                .id(user.id())
                .username(user.username())
                .build();
        UserResponse result = userService.getById(user.id());
        Assertions.assertEquals(expected, result);
        Mockito.verify(userRepository).getById(user.id());
    }

    @Test
    void getAllByUsername_success() {
        final User user = getValidUser();
        Set<Long> ids = Set.of(user.id());
        Mockito.when(userRepository.getAllByIds(ids))
                .thenReturn(Collections.singletonList(user));
        Set<UserResponse> userSet = userService.getAllByIds(ids);
        Assertions.assertEquals(Set.of(UserResponse.builder()
                .id(user.id())
                .username(user.username())
                .build()), userSet);
        Mockito.verify(userRepository).getAllByIds(ids);
    }

    @Test
    void delete_success() {
        final User user = getValidUser();
        Mockito.when(userRepository.delete(user.id())).thenReturn(user);
        ServiceResponse serviceResponse = userService.delete(user.id());
        ServiceResponse expected = ServiceResponse.builder()
                .success(true)
                .build();
        Assertions.assertEquals(expected, serviceResponse);
        Mockito.verify(userRepository).delete(user.id());
    }

    @Test
    void create_error_nullRequest() {
        final RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.create(null)
        );
        Assertions.assertEquals("Введите информацию о пользователе", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullUsername() {
        final UserRequest userRequest = new UserRequest(null, "pass");
        final RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.create(userRequest)
        );
        Assertions.assertEquals("Заполните имя пользователя", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullPassword() {
        final UserRequest userRequest = new UserRequest("name", null);
        final RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.create(userRequest)
        );
        Assertions.assertEquals("Заполните пароль пользователя", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_shortPassword() {
        final UserRequest userRequest = new UserRequest("name", "12");
        final RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.create(userRequest)
        );
        Assertions.assertEquals("Минимальная длина пароля: 3 символа", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    private User getValidUser() {
        return User.builder()
                .id(1L)
                .username("Test")
                .passwordHash("hash")
                .build();
    }
}