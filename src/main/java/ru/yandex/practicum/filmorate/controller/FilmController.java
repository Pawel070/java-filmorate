package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController implements ControllerInterface<Film> {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/help")
    public Map<String, Integer> feed() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }

    @Override
    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
        log.info("Контроллер GET фильм по Id> {}", id);
        return filmService.findFilmById(id);
    }

    @Override
    @GetMapping
    public Collection<Film> selectGetting() {
        log.info("Запрос всех фильмов");
        return filmService.returnAllFilms();
    }

    @Override
    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Обновление информации о фильме {}", film);
        return filmStorage.update(film);
    }

    @Override
    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Добавление фильма {}", film);
        return filmStorage.create(film);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("Удаление фильма из базы по id {}", id);
        filmStorage.deleteByIdFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeUserById(@PathVariable int id, @PathVariable int userId) {
        log.info("Контроллер PUT лайк фильму ставит> User с Id {} - {}", id, userId);
        filmStorage.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeUserById(@PathVariable int id, @PathVariable int userId) {
        log.info("Контроллер DELETE лайк фильму удаляет> {} , {}", id, userId);
        filmStorage.deleteLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    @Validated
    public Collection<Film> maxLikeFilm(@PathVariable @RequestParam(defaultValue = "10") int count) {
        log.info("Контроллер GET  список из первых  по количеству лайков> {}", count);
        return filmService.maxLikeFilm(count);
    }

}