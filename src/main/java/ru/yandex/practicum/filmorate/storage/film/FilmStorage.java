package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;


public interface FilmStorage {

    Film newFilm(Film film);

    Film changeFilm(Film film);

    Film getFilm(int id);

}
