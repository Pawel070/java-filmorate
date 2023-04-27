package ru.yandex.practicum.filmorate.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ErrorsIO.IncorrectParameterException;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dataService.ConstantsSort.DESCENDING_ORDER;

@Slf4j
@Service
public class FilmService {

    @Autowired
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    private FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Collection<Film> returnAllFilms() {
        log.info("returnAllFilms Ok. {}", inMemoryFilmStorage.getFilms().values());
        return inMemoryFilmStorage.getFilms().values();
    }

    public Film findFilmById(String filmId) {
        Film filmReturn;
        if (filmId == null) {
            log.info("Запрос пользователя фильм по Id - неправильный формат Id: {}", filmId);
            filmReturn = null;
            throw new IncorrectParameterException("Запрос пользователя фильм по Id - неправильный формат Id.");
        } else {
            int id = Integer.parseInt(filmId);
            if (id <= 0) {
                log.info("Запрос пользователя фильм по Id - отрицательное или 0 значение Id: {}", filmId);
                filmReturn = null;
                throw new IncorrectParameterException("Запрос пользователя фильм по Id - неправильный формат Id.");
            } else {
                filmReturn = inMemoryFilmStorage.getFilms().get(id);
                if (filmReturn == null) {
                    log.info("Запрос пользователя фильм по Id - фильма нет. {}", filmId);
                    throw new MethodArgumentNotException("Запрос пользователя фильм по Id с таким Id нет.");
                }
            }
        }
        log.info("Запрос пользователя по Id Ok. {}", filmReturn);
        return filmReturn;
    }

    public Film addLikeUserById(String userId, String like) {
        Film filmReturn1 = findFilmById(userId);
        Set<Long> likes = filmReturn1.getLikes();
        if (filmReturn1 == null) {
            log.info("Запрос добавления лайка по Id фильма невыполним. {}", filmReturn1);
            throw new MethodArgumentNotException("Запрос добавления лайка по Id фильма невыполним - фильма с таким Id нет.");
        } else {
            likes.add(Long.parseLong(like));
            filmReturn1.setLikes(likes);
            inMemoryFilmStorage.changeFilm(filmReturn1);
            log.info("Запрос добавления лайка по Id фильма Ok. {} < {}", filmReturn1);
        }
        return filmReturn1;
    }

    public Film deleteLikeUserById(String userId, String like) {
        Film filmReturn1 = findFilmById(userId);
        Set<Long> likes = filmReturn1.getLikes();
        if (filmReturn1 == null) {
            log.info("Запрос удаления лайка по Id фильма невыполним. {}", filmReturn1);
            throw new MethodArgumentNotException("Запрос удаления лайка по Id фильма невыполним - фильма с таким Id нет.");
        } else {
            likes.remove(Long.parseLong(like));
            filmReturn1.setLikes(likes);
            inMemoryFilmStorage.changeFilm(filmReturn1);
            log.info("Запрос удаления лайка по Id фильма Ok. {} ", filmReturn1);
        }
        return filmReturn1;
    }

    public Collection<Film> maxLikeFilm(String like) {
        log.info("Запрос самых лайковых фильмов в количестве {} шт. Ok.", like);
        Collection<Film> sortMaxLike = inMemoryFilmStorage.getFilms().values().stream()
                .filter(film -> film.getLikes().contains(Long.parseLong(like))).collect(Collectors.toList());
        return sortMaxLike;
    }

}
