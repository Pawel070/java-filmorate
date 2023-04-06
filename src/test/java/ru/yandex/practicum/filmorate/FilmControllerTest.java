package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.Servis.ServisDate.localDateTimeMinFilm;

class FilmControllerTest {

    Map<Integer, Film> filmMap;
    LocalDateTime localDateTime;
    Film film;
    Film film1;
    Film film2;
    FilmController filmController = new FilmController();

    @BeforeEach
    void BeforeEach() {

        filmMap = new HashMap<>();
        localDateTime = null;

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
        Collection<Film> test = filmController.returnAllFilms();
        Assertions.assertNotNull(filmMap.values());
    }

    @Test
    void newFilmTest() throws ValidationException {
        Film test = filmController.newFilm(film);
        Assertions.assertEquals("f1", test.getName());
    }

    @Test
    void filmValidate2Test() {
        Film test = filmController.newFilm(film);
        film1.setId(1);
        try {
            test = filmController.newFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E02 Фильм с таким ID уже внесён. Смените ID.", exception.getS());
        }
        film1.setId(2);
        film.setReleaseDate(LocalDateTime.of(1001, 1, 1, 0, 1)); // портим
        try {
            test = filmController.newFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E04 Дата релиза должна быть не ранее " +
                    localDateTimeMinFilm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), exception.getS());
        }
    }

    @Test
    void changeFilmTest() throws ValidationException {
        film1.setId(1);
        Film test = filmController.changeFilm(film1);
        Assertions.assertEquals("f2", test.getName());
    }

    @Test
    void changeFilmValidate2Test() {
        film1.setId(4);
        try {
            Film test = filmController.changeFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E05 Фильм с таким ID не существует. Смените ID.", exception.getS());
        }
    }

    @Test
    void findAll() {
    }

    @Test
    void createFilm() {
    }

    @Test
    void updateFilm() {
    }
}