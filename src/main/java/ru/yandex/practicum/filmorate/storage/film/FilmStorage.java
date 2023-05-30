package ru.yandex.practicum.filmorate.storage.film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film create(Film film);

    long getCollectionSize();

    List<Film> getFilmsToRate(int IdRate);

    Collection<Film> getCollectionFilm();

    void deleteByIdFilm(int idFilm);

    Collection<Film> getMaxPopular(int scoring);

    Film getByIdFilm(int idFilm);

    Film update(Film film);

    Set<Long> getLikes(int idFilm);

    void addLike(int idFilm, int idUser);

    void deleteLike(int idFilm, int idUser);

    boolean getLikeExist(int idFilm, int idUser);

    Optional<List<Film>> searchByAll(String query);

    Optional<List<Film>> searchByName(String query);

    Optional<List<Film>> searchByDirector(String query);

    List<Film> getCommonFilms(int userId);


    List<Film> getFilmsByDirector(int directorId, String sorting);

}