package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.dataService.DateTimeConfiguration.localDateMinFilm;

@Data
@Slf4j
@Validated
@Repository
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();

    private static int numberId = 0;

    private int servisId() {
        numberId++;
        log.info("set ServisId: {}", numberId);
        return numberId;
    }

    @Override
    public Film newFilm(@Valid @RequestBody Film film) {
        Boolean validIO = true;
        Film retFilm;
        log.info("newFilm: {}", film);
        film.setId(servisId());
        if (films.containsKey(film.getId()) && (validIO == true)) {
            log.error("ERR01 {}", films.containsKey(film.getId()));
            validIO = false;
            throw new ValidationException("E01 Фильм с таким ID уже внесён. Смените ID.");
        }
        if ((film.getReleaseDate().compareTo(localDateMinFilm) <= 0) && (validIO == true)) {
            log.error("ERR04 - {}", film.getReleaseDate());
            validIO = false;
            throw new ValidationException("E04 Дата релиза должна быть не ранее {}" +
                    localDateMinFilm.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (validIO == true) {
            films.put(film.getId(), film);
            log.info("newFilm post Ok. {}", film);
            retFilm = film;
            return retFilm;
        } else {
            log.error("newFilm not post`s {}", film);
            return null;
        }
    }

    @Override
    public Film changeFilm(@Valid @RequestBody Film film) {
        Boolean validIO = true;
        log.info("changeFilm: {}", film);
        int buferId = film.getId();
        log.info("buferId: {}", buferId);
        log.info("films.containsKey(buferId): {}", films.containsKey(buferId));
        if (films.containsKey(buferId) != true && (validIO == true)) {
            log.error("ERR05 - {}", films.containsKey(buferId));
            validIO = false;
            throw new MethodArgumentNotException("E05 Фильм с таким ID не существует. Смените ID.");
        }
        if (validIO == true) {
            films.remove(buferId);
             films.put(buferId, film);
            log.info("changeFilm put Ok. {}", film);
            return film;
        } else {
            log.error("changeFilm not put`s {}", films.get(buferId));
            return null;
        }
    }

    @Override
    public Film getFilm(int id) {
        log.info("getFilm: {}", id);
        return films.get(id);
    }

}
