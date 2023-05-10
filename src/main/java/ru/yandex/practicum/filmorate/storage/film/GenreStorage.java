package ru.yandex.practicum.filmorate.storage.film;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreStorage {

    List<Genre> findByIdFilm(int idFilm);

    Collection<Genre> getGenres();

    Genre checkGenre(int idGenre);

}
