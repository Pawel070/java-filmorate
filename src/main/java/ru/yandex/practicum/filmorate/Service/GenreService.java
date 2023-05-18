package ru.yandex.practicum.filmorate.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class GenreService {

    private final GenreStorage storage;

    @Autowired
    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public Collection<Genre> findAll() {
        log.info("Возврат списка жанров.");
        return storage.getGenres();
    }

    public List<Genre> findById(int id) {
        log.info("Возврат списка жанров фильма с id {} ", id);
        return storage.findGenreByIdFilm(id);
    }

    public Genre getGenreById(int id) {
        log.info("Возврат списка жанров фильма с id {} ", id);
        return storage.checkGenre(id);
    }

}
