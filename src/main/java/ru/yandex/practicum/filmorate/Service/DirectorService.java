package ru.yandex.practicum.filmorate.Service;

import java.util.Collection;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.RateStorage;


@Data
@Slf4j
@Service
public class DirectorService {

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private DirectorService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Director> returnAllDirector() {         // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        log.info("returnAllDirector Ok.");
        return  null;
    }

    public void validateD(Director director) throws ValidationException {
        if (director.getName().isBlank()) {
            log.info("ValidationException: {}", "Имя режисёра не может быть пустым");
            throw new ValidationException("Имя режисёра не может быть пустым");
        }
        if (director.getName().isBlank() || director.getName().length() > 250) {
            log.info("ValidationException: {}", "Максимальная длина  — 250 символов");
            throw new ValidationException("Максимальная длина  — 250 символов");
        }
    }

}
