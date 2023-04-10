package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.yandex.practicum.filmorate.dataAllProject.DateTimeConfiguration.localDateMinFilm;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();
    private static int numberId = 0;

    private int servisId() {
        numberId++;
        log.info("set ServisId: {}", numberId);
        return numberId;
    }

    @GetMapping
    public Collection<Film> returnAllFilms() {
        log.info("returnAllFilms Ok. {}", films.values());
        return films.values();
    }

    @PostMapping
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

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        Boolean validIO = true;
        log.info("changeFilm: {}", film);
        int buferId = film.getId();
        log.info("buferId: {}", buferId);
        log.info("films.containsKey(buferId): {}", films.containsKey(buferId));
        if (films.containsKey(buferId) != true && (validIO == true)) {
            log.error("ERR05 - {}", films.containsKey(buferId));
            validIO = false;
            throw new ValidationException("E05 Фильм с таким ID не существует. Смените ID.");
        }
        log.info("далее.... ");
        if (validIO == true) {
            log.info("film удаляем ... {}", films.get(buferId));
            films.remove(buferId);
            log.info("film новый заносим ... {}", film);
            films.put(buferId, film);
            log.info("changeFilm put Ok. {}", film);
            return film;
        } else {
            log.error("changeFilm not put`s {}", films.get(buferId));
            return null;
        }
    }

}