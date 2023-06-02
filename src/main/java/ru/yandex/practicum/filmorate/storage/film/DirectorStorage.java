package ru.yandex.practicum.filmorate.storage.film;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.Director;

public interface DirectorStorage {

    Director create(Director director);

    long getCollectionSize();

    Collection<Director> getCollectionDirector();

    void deleteByIdDirector(int idD);

    Director getByIdDirector(int idD);

    Director update(Director director);

    List<Director> findDirectorsByIdFilm(int idFilm);

    Director checkDirector(int idD);

}