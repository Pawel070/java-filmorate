package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.model.Film;


public interface FilmStorage {

    Film newFilm(@Valid @RequestBody Film film);

    Film changeFilm(@Valid @RequestBody Film film);

    Film getFilm(int id);

}
