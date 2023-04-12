package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/films")
public class FilmController {

    private FilmStorage inMemoryFilmStorage;
    private UserService userService;
    private FilmService filmService;

    @Autowired
    public void FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/help")
    public Map<String, Integer> feed() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable String id) {
        log.info("Контроллер GET фильм по Id> {}", id);
        return filmService.findFilmById(id);
    }

    @GetMapping
    public Collection<Film> returnAllFilms() {
        return filmService.returnAllFilms();
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return inMemoryFilmStorage.changeFilm(film);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return inMemoryFilmStorage.newFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikeUserById(@PathVariable String id, @PathVariable String userId) {
        log.info("Контроллер PUT лайк фильму ставит> {} - {}", id, userId);
        return filmService.addLikeUserById(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeUserById(@PathVariable String id, @PathVariable String userId) {
        log.info("Контроллер DELETE лайк фильму удаляет> {} , {}", id, userId);
        return filmService.deleteLikeUserById(id, userId);
    }

    @GetMapping("/popular?count={count}")
    public Collection<Film> maxLikeFilm(@PathVariable Optional<String> count ) {
        String countStr = count.orElse("10");
        log.info("Контроллер GET  список из первых  по количеству лайков> {}", countStr);
        return filmService.maxLikeFilm(countStr);
    }

}