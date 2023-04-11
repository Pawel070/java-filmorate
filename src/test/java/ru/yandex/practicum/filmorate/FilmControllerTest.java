package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.dataService.DateTimeConfiguration.localDateMinFilm;

class FilmControllerTest {

    Map<Integer, Film> filmMap;
    LocalDateTime localDateTime;
    Film film;
    Film film1;
    Film film2;
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @BeforeEach
    void BeforeEach() {

        filmMap = new HashMap<>();
        localDateTime = null;

        film = Film.builder() // первый
                .id(1)
                .name("f1")
                .description("описание 1")
                .releaseDate(LocalDate.from(LocalDateTime.of(1896, 1, 1, 0, 0)))
                .duration(10)
                .build();
        film1 = Film.builder() // на замену
                .id(2)
                .name("f2")
                .description("описание 2")
                .releaseDate(LocalDate.from(LocalDateTime.of(1897, 1, 1, 0, 0)))
                .duration(20)
                .build();

        film2 = Film.builder() // контрольный
                .id(1)
                .name("f3")
                .description("описание 3")
                .releaseDate(LocalDate.from(LocalDateTime.of(1898, 1, 1, 0, 0)))
                .duration(30)
                .build();
    }

    @Test
    void newFilmTest() throws ValidationException {
        Film test = inMemoryFilmStorage.newFilm(film);
        Assertions.assertEquals("f1", test.getName());
    }

    @Test
    void filmValidate2Test() {
        Film test = inMemoryFilmStorage.newFilm(film);
        film1.setId(1);
        try {
            test = inMemoryFilmStorage.newFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E02 Фильм с таким ID уже внесён. Смените ID.", exception.getMessage());
        }
        film1.setId(2);
        film.setReleaseDate(LocalDate.from(LocalDateTime.of(1001, 1, 1, 0, 1))); // портим
        try {
            test = inMemoryFilmStorage.newFilm(film1);
        } catch (ValidationException exception) {
            Assertions.assertEquals("E04 Дата релиза должна быть не ранее " +
                    localDateMinFilm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), exception.getMessage());
        }
    }

    @Test
    void changeFilmTest() throws ValidationException {
        film = inMemoryFilmStorage.newFilm(film);
        Film test = inMemoryFilmStorage.changeFilm(film);
        Assertions.assertEquals("f1", test.getName());
    }

    @Test
    void changeFilmValidate2Test() {
        Film test = null;
        film = inMemoryFilmStorage.newFilm(film);
        film.setId(1);
        try {
            test = inMemoryFilmStorage.changeFilm(film);
        } catch (MethodArgumentNotException exception) {
            Assertions.assertEquals("E05 Фильм с таким ID не существует. Смените ID.", exception.getMessage());
        }
    }

}