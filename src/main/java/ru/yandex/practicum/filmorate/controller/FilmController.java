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

    protected Map<Integer, Film> filmMap = new HashMap<>();
    private final List<Film> films = new ArrayList<>();
    public static int numberId = 0;

    public int servisId() {
        numberId++;
        return numberId;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return returnAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        return newFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return changeFilm(film);
    }

    public Collection<Film> returnAllFilms() {
        return filmMap.values();
    }

    public Film newFilm(Film film) throws ValidationException {
        Film retFilm;
        film.setId(servisId());
        if (filmMap.containsKey(film.getId())) {
            throw new ValidationException("E02 Фильм с таким ID уже внесён. Смените ID.");
        }
        if (film.getReleaseDate().compareTo(localDateTimeMinFilm) <= 0) {
            throw new ValidationException("E04 Дата релиза должна быть не ранее " +
                    localDateTimeMinFilm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        }
        filmMap.put(film.getId(), film);
        retFilm = film;
        return retFilm;
    }

    public Film changeFilm(Film film) throws ValidationException {
        Film retFilm;
        if ((filmMap.containsKey(film.getId()))) {
            throw new ValidationException("E05 Фильм с таким ID не существует. Смените ID..");
        }
        Film filmBufer = filmMap.get(film.getId());
        filmMap.remove(film.getId());
        if (newFilm(film) == null) {
            filmMap.put(filmBufer.getId(), filmBufer);
            throw new ValidationException("E06 Изменения не внесены.");
        }
        retFilm = film;
        return retFilm;
    }

}