package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    DataCheck dataCheck = new DataCheck();

    @GetMapping
    public Collection<Film> findAll() {
        return dataCheck.returnAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @ RequestBody Film film) throws ValidationException {
        return dataCheck.newFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return dataCheck.changeFilm(film);
    }



}