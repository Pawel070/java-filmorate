package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.yandex.practicum.filmorate.dataService.DateTimeConfiguration.localDateMinFilm;

@Data
@Slf4j
@Validated
@Repository
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();

    private int numberId;

    private int servisId() {
        numberId++;
        log.info("set ServisId: {}", numberId);
        return numberId;
    }

    public Film newFilm(@Valid @RequestBody Film film) {
        log.info("newFilm: {}", film);
        film.setIdFilm(servisId());
        if (films.containsKey(film.getIdFilm())) {
            log.error("ERR01 {}", films.containsKey(film.getIdFilm()));
            throw new ValidationException("E01 Фильм с таким ID уже внесён. Смените ID.");
        }
        if (film.getReleaseDate().compareTo(localDateMinFilm) <= 0) {
            log.error("ERR04 - {}", film.getReleaseDate());
            throw new ValidationException("E04 Дата релиза должна быть не ранее {}" +
                    localDateMinFilm.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        films.put(film.getIdFilm(), film);
        log.info("newFilm post Ok. {}", film);
        return film;
    }

    public Film changeFilm(@Valid @RequestBody Film film) {
        Film retFilm;
        log.info("changeFilm: {}", film);
        int buferId = film.getIdFilm();
        log.info("buferId: {}", buferId);
        log.info("films.containsKey(buferId): {}", films.containsKey(buferId));
        if (films.containsKey(buferId)) {
            films.remove(buferId);
            films.put(buferId, film);
            log.info("changeFilm put Ok. {}", film);
        } else {
            log.error("ERR05 - {}", films.containsKey(buferId));
            throw new MethodArgumentNotException("E05 Фильм с таким ID не существует. Смените ID.");
        }
        return film;
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public long getCollectionSize() {
        return 0;
    }

    @Override
    public List<Film> getFilmsToRate(int IdRate) {
        return null;
    }

    @Override
    public Collection<Film> getCollectionFilm() {
        return null;
    }

    @Override
    public void deleteByIdFilm(int idFilm) {

    }

    @Override
    public Collection<Film> getMaxPopular(int scoring) {
        return null;
    }

    @Override
    public Film getByIdFilm(int idFilm) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Set<Long> getLikes(int idFilm) {
        return null;
    }

    @Override
    public void addLike(int idFilm, int idUser) {

    }

    @Override
    public void deleteLike(int idFilm, int idUser) {

    }

    @Override
    public boolean getLikeExist(int idFilm, int idUser) {
        return false;
    }
}
