package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class UserControllerTest {

    Map<Integer, User> userMap;
    LocalDateTime localDateTime;
    User user;
    User user1;
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @BeforeEach
    void BeforeEach() {

        userMap = new HashMap<>();
        localDateTime = null;

        user = User.builder() // первый
                .id(1)
                .login("u1")
                .email("u1@test.ru")
                .birthday(LocalDate.from(LocalDateTime.of(1970, 1, 1, 0, 0)))
                .name("user1")
                .build();

        user1 = User.builder() // на замену
                .id(2)
                .login("u2")
                .email("u2@test.ru")
                .birthday(LocalDate.from(LocalDateTime.of(1980, 1, 1, 0, 0)))
                .name("user2")
                .build();
    }

    @Test
    void newUserTest() throws ValidationException {
        User test = inMemoryUserStorage.newUser(user);
        Assertions.assertEquals("user1", test.getName());
    }

    @Test
    void changeUserTest() throws ValidationException {
        user = inMemoryUserStorage.newUser(user);
        user.setId(1);
        user = inMemoryUserStorage.changeUser(user);
        Assertions.assertEquals("user1", user.getName());
    }

    @Test
    void changeUserValidateTest() {
        user = inMemoryUserStorage.newUser(user);
        user.setId(4);
        try {
            user = inMemoryUserStorage.changeUser(user);
        } catch (MethodArgumentNotException exception) {
            Assertions.assertNotNull(user);
        }
    }

    @Test
    void userValidateIdDubleTest() throws ValidationException {
        User test = inMemoryUserStorage.newUser(user);
        user1.setId(1);
        try {
            test = inMemoryUserStorage.newUser(user1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E09 Пользователь с таким ID уже внесён. Смените ID.", exception.getMessage());
        }
    }

    @Test
    void userValidateLoginSpaseTest() {
        user.setLogin("u 1");
        try {
            User test = inMemoryUserStorage.newUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E07 Логин не может быть пустым или содержать пробелы.", exception.getMessage());
        }
    }

}