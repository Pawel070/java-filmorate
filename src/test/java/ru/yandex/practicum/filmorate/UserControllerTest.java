package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class UserControllerTest {

    Map<Integer, User> userMap;
    LocalDateTime localDateTime;
    User user;
    User user1;
    UserController userController = new UserController();

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
    void returnAllUsersTest() {
        Collection<User> test = userController.returnAllUsers();
        Assertions.assertNotNull(userMap.values());
    }

    @Test
    void newUserTest() throws ValidationException {
        User test = userController.newUser(user);
        Assertions.assertEquals("user1", test.getName());
    }

    @Test
    void changeUserTest() throws ValidationException {
        user1.setId(1);
        User test = userController.changeUser(user1);
        Assertions.assertEquals("user2", test.getName());
    }

    @Test
    void changeUserValidateTest() {
        user1.setId(4);
        try {
            User test = userController.changeUser(user1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E05 Фильм с таким ID не существует. Смените ID.", exception.getS());
        }
    }

    @Test
    void userValidateIdDubleTest() throws ValidationException {
        User test = userController.newUser(user);
        user1.setId(1);
        try {
            test = userController.newUser(user1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E09 Пользователь с таким ID уже внесён. Смените ID.", exception.getS());
        }
    }

    @Test
    void userValidateLoginSpaseTest() {
        user.setLogin("u 1");
        try {
            User test = userController.newUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E07 Логин не может быть пустым или содержать пробелы.", exception.getS());
        }
    }

    @Test
    void findAll() {

    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }
}