package ru.yandex.practicum.filmorate.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.ErrorsIO.IncorrectParameterException;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.RateStorage;

import java.util.*;

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
    private EventService eventService;

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
        eventService.createEvent(userId, EventType.LIKE, EventOperation.ADD, filmId);
        log.info("Запрос добавления лайка по Id фильма Ok.");
    }

    private void deleteLikeUserById(int userId, int filmId) {
        Film filmReturn = filmStorage.getByIdFilm(filmId);
        if (filmReturn == null) {
            log.info("Запрос добавления лайка по Id фильма невыполним. {}", filmReturn);
            throw new MethodArgumentNotException("Запрос добавления лайка по Id фильма невыполним - фильма с таким Id нет.");
        }
        filmStorage.deleteLike(filmId, userId);
        eventService.createEvent(userId, EventType.LIKE, EventOperation.REMOVE, filmId);
        log.info("Удаление лайка по Id фильма Ok.");
    }

    public Collection<Film> maxLikeFilm(int like) {
        log.info("Запрос самых лайковых фильмов в количестве {} шт. Ok.", like);
        return filmStorage.getMaxPopular(like);
    }

}
