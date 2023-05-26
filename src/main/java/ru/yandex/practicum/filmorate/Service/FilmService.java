package ru.yandex.practicum.filmorate.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.ErrorsIO.IncorrectParameterException;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.RateStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service
public class FilmService {

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private GenreStorage genreStorage;

    @Autowired
    private RateStorage rateStorage;

    @Autowired
    private FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> returnAllFilms() {
        log.info("returnAllFilms Ok.");
        return filmStorage.getCollectionFilm();
    }

    public Film findFilmById(int filmId) {
        Film filmReturn;
        if (filmId < 1) {
            log.info("Запрос пользователя фильм по Id - неправильный формат Id: {}", filmId);
            throw new IncorrectParameterException("Запрос пользователя фильм по Id - неправильный формат Id.");
        }
        filmReturn = filmStorage.getByIdFilm(filmId);
        if (filmReturn == null) {
            log.info("Запрос пользователя фильм по Id - фильма нет. {}", filmId);
            throw new MethodArgumentNotException("Запрос пользователя фильм по Id с таким Id нет.");
        }
        log.info("Запрос пользователя по Id Ok.");
        return filmReturn;
    }

    private void addLikeUserById(int userId, int filmId) {
        Film filmReturn = filmStorage.getByIdFilm(filmId);
        if (filmReturn == null) {
            log.info("Запрос добавления лайка по Id фильма невыполним. {}", filmReturn);
            throw new MethodArgumentNotException("Запрос добавления лайка по Id фильма невыполним - фильма с таким Id нет.");
        }
        filmStorage.addLike(filmId, userId);
        log.info("Запрос добавления лайка по Id фильма Ok.");
    }

    private void deleteLikeUserById(int userId, int filmId) {
        Film filmReturn = filmStorage.getByIdFilm(filmId);
        if (filmReturn == null) {
            log.info("Запрос добавления лайка по Id фильма невыполним. {}", filmReturn);
            throw new MethodArgumentNotException("Запрос добавления лайка по Id фильма невыполним - фильма с таким Id нет.");
        }
        filmStorage.deleteLike(filmId, userId);
        log.info("Удаление лайка по Id фильма Ok.");
    }

    public Collection<Film> maxLikeFilm(int like) {
        log.info("Запрос самых лайковых фильмов в количестве {} шт. Ok.", like);
        return filmStorage.getMaxPopular(like);
    }

    public void validateF(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.info("ValidationException: {}", "Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().isBlank() || film.getDescription().length() > 200) {
            log.info("ValidationException: {}", "Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("ValidationException: {}", "Дата релиза должна быть не раньше 28.12.1895");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            log.info("ValidationException: {}", "Продолжительность должна быть положительной");
            throw new ValidationException("Продолжительность должна быть положительной");
        }
    }

}
