package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.controller.DataCheck;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.FilmorateApplication.localDateTimeMinFilm;

class DataCheckTest {

    Map<Integer, Film> filmMap;
    Map<Integer, User> userMap;
    DataCheck dataCheck;
    LocalDateTime localDateTime;
    User user;
    User user1;
    Film film;
    Film film1;
    Film film2;

    @BeforeEach
    void BeforeEach() {

        filmMap = new HashMap<>();
        userMap = new HashMap<>();
        localDateTime = null;
        dataCheck = new DataCheck();

        user = User.builder() // первый
                .id(1)
                .login("u1")
                .email("u1@test.ru")
                .birthday(LocalDateTime.of(1970, 1, 1, 0, 0))
                .name("user1")
                .build();

        user1 = User.builder() // на замену
                .id(2)
                .login("u2")
                .email("u2@test.ru")
                .birthday(LocalDateTime.of(1980, 1, 1, 0, 0))
                .name("user2")
                .build();

        film = Film.builder() // первый
                .id(1)
                .name("f1")
                .description("описание 1")
                .releaseDate(LocalDateTime.of(1896, 1, 1, 0, 0))
                .duration(10)
                .build();
        film1 = Film.builder() // на замену
                .id(2)
                .name("f2")
                .description("описание 2")
                .releaseDate(LocalDateTime.of(1897, 1, 1, 0, 0))
                .duration(20)
                .build();

        film2 = Film.builder() // контрольный
                .id(1)
                .name("f3")
                .description("описание 3")
                .releaseDate(LocalDateTime.of(1898, 1, 1, 0, 0))
                .duration(30)
                .build();
    }

    @Test
    void returnAllFilmsTest() {
        Collection<Film> test = dataCheck.returnAllFilms();
        Assertions.assertNotNull(filmMap.values());
    }

    @Test
    void newFilmTest() throws ValidationException {
        Film test = dataCheck.newFilm(film);
        Assertions.assertEquals("f1", test.getName());
    }

    @Test
    void filmValidate5Test() {
        film.setName(""); // портим
        try {
            Film test = dataCheck.newFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E01 Название фильма не может быть пустым.", exception.getS());
        }
        film.setName("f1"); // восстанавливаем
        film.setDescription("0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-" +
                "0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-" +
                "0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-0123456789-"); // 264 символа
        try {
            Film test = dataCheck.newFilm(film);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E11 Длина описания не более 200 символов.", exception.getS());
        }
        film.setDescription("описание 1");
        film1.setId(1);
        try {
            Film test = dataCheck.newFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E02 Фильм с таким ID уже внесён. Смените ID.", exception.getS());
        }
        film1.setId(2);
        film1.setDuration(0);
        try {
            Film test = dataCheck.newFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E03 Продолжительность фильма должна быть положительной и не 0.", exception.getS());
        }
        film1.setDuration(20);
        film1.setReleaseDate(LocalDateTime.of(1001, 1, 1, 0, 1)); // портим
        try {
            Film test = dataCheck.newFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E04 Дата релиза должна быть не ранее " +
                    localDateTimeMinFilm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), exception.getS());
        }
    }

    @Test
    void changeFilmTest() throws ValidationException {
        film1.setId(1);
        Film test = dataCheck.changeFilm(film1);
        Assertions.assertEquals("f2", test.getName());
        Assertions.assertEquals(1, test.getId());
    }

    @Test
    void changeFilmValidate2Test() {
        film1.setId(4);
        try {
            Film test = dataCheck.changeFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E05 Фильм с таким ID не существует. Смените ID.", exception.getS());
        }
    }

    @Test
    void returnAllUsersTest() {
        Collection<User> test = dataCheck.returnAllUsers();
        Assertions.assertNotNull(userMap.values());
    }

    @Test
    void newUserTest() throws ValidationException {
        User test = dataCheck.newUser(user);
        Assertions.assertEquals("user1", test.getName());
    }

    @Test
    void changeUserTest() throws ValidationException {
        user1.setId(1);
        User test = dataCheck.changeUser(user1);
        Assertions.assertEquals("user2", test.getName());
        Assertions.assertEquals(1, test.getId());
    }

    @Test
    void changeUserValidateTest() {
        user1.setId(4);
        try {
            User test = dataCheck.changeUser(user1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E05 Фильм с таким ID не существует. Смените ID.", exception.getS());
        }
    }

    @Test
    void userValidateIdDubleTest() throws ValidationException {
        User test = dataCheck.newUser(user);
        user1.setId(1);
        try {
            test = dataCheck.newUser(user1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E09 Пользователь с таким ID уже внесён. Смените ID.", exception.getS());
        }
    }

    @Test
    void userValidateEmail0Test() {
        user.setEmail("");
        try {
            User test = dataCheck.newUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E08 Электронная почта не может быть пустой и должна содержать символ @.", exception.getS());
        }
    }

    @Test
    void userValidateEmailAmpSpaseTest() {
        user.setLogin("user#test.ru");
        try {
            User test = dataCheck.newUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E08 Электронная почта не может быть пустой и должна содержать символ @.", exception.getS());
        }
    }

    @Test
    void userValidateLogin0Test() {
        user.setLogin("");
        try {
            User test = dataCheck.newUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E07 Логин не может быть пустым или содержать пробелы.", exception.getS());
        }
    }

    @Test
    void userValidateLoginSpaseTest() {
        user.setLogin("u 1");
        try {
            User test = dataCheck.newUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E07 Логин не может быть пустым или содержать пробелы.", exception.getS());
        }
    }

    @Test
    void userValidate2Test() {
        user.setLogin("u1");
        user.setName("");
        try {
            User test = dataCheck.newUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E12 Имя не должно быть пустым. Используется Логин.", exception.getS());
        }
        user1.setBirthday(LocalDateTime.of(2023, 10, 10, 0, 1)); // портим
        try {
            User test = dataCheck.newUser(user1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E10 Дата рождения не может быть в будущем.", exception.getS());
        }

    }

}