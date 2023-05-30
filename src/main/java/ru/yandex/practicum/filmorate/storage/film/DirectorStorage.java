package ru.yandex.practicum.filmorate.storage.film;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

public interface DirectorStorage {

    Director create(Director director);

    long getCollectionSize();

    Collection<Director> getCollectionDirector();

    void deleteByIdDirector(int idD);

    Director getByIdDirector(int idD);

    Director update(Director director);

    List<Director> findDirectorsByIdFilm(int idFilm);

}