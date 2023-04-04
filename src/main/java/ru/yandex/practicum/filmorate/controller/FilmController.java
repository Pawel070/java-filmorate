package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Data;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.yandex.practicum.filmorate.Servis.ServisDate.localDateTimeMinFilm;

@Data
@RestController
@RequestMapping("/films")
public class FilmController {

    protected Map<Integer, Film> films = new HashMap<>();
    private static int numberId = 0;

    private int servisId() {
        numberId++;
        return numberId;
    }

    @GetMapping
    public Collection<Film> returnAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film newFilm(@Valid @RequestBody Film film) throws ValidationException {
        Film retFilm;
        film.setId(servisId());
        if (films.containsKey(film.getId())) {
            throw new ValidationException("E02 Фильм с таким ID уже внесён. Смените ID.");
        }
        if (film.getReleaseDate().compareTo(localDateTimeMinFilm) <= 0) {
            throw new ValidationException("E04 Дата релиза должна быть не ранее " +
                    localDateTimeMinFilm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        }
        films.put(film.getId(), film);
        retFilm = film;
        return retFilm;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) throws ValidationException {
        Film retFilm;
        if ((films.containsKey(film.getId()))) {
            throw new ValidationException("E05 Фильм с таким ID не существует. Смените ID..");
        }
        Film filmBufer = films.get(film.getId());
        films.remove(film.getId());
        if (newFilm(film) == null) {
            films.put(filmBufer.getId(), filmBufer);
            throw new ValidationException("E06 Изменения не внесены.");
        }
        retFilm = film;
        return retFilm;
    }

}