package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.FilmorateApplication.localDateTimeMinFilm;

@Slf4j
@Data
@Service
public class DataCheck {

    protected Map<Integer, Film> filmMap = new HashMap<>();
    protected Map<Integer, User> userMap = new HashMap<>();

    public Collection<Film> returnAllFilms() {
        return filmMap.values();
    }

    public Film newFilm(Film film) throws ValidationException {
        Film retFilm;
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("E01 Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            film.setDescription(film.getDescription().substring(0, 199));
            throw new ValidationException("E11 Длина описания не более 200 символов.");
        }
        if (filmMap.containsKey(film.getId())) {
            throw new ValidationException("E02 Фильм с таким ID уже внесён. Смените ID.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("E03 Продолжительность фильма должна быть положительной и не 0.");
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
    public Collection<User> returnAllUsers() {
        return userMap.values();
    }

    public User newUser(User user) throws ValidationException {
        User retUser;
        if (user.getLogin() == null && (!user.getLogin().contains(" "))) {
            throw new ValidationException("E07 Логин не может быть пустым или содержать пробелы.");
        }
        if (user.getName().trim().length() == 0) {
            user.setName(user.getLogin());
            throw new ValidationException("E12 Имя не должно быть пустым. Используется Логин.");
        }
        if ((user.getEmail().trim().length() == 0) || (user.getLogin().contains("@"))){
            throw new ValidationException("E08 Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (userMap.containsKey(user.getId())) {
            throw new ValidationException("E09 Пользователь с таким ID уже внесён. Смените ID.");
        }
        if (user.getBirthday().compareTo(LocalDateTime.now()) > 0) {
            throw new ValidationException("E10 Дата рождения не может быть в будущем.");
        }
        userMap.put(user.getId(), user);
        retUser = user;
        return retUser;
    }

    public User changeUser(User user) throws ValidationException {
        User retUser;
        if ((userMap.containsKey(user.getId()))) {
            throw new ValidationException("E05 Пользователь с таким ID не существует. Смените ID.");
        }
        User userBufer = userMap.get(user.getId());
        userMap.remove(user.getId());
        if (newUser(user) == null) {
            userMap.put(user.getId(), userBufer);
            throw new ValidationException("E06 Изменения не внесены.");
        }
        retUser = user;
        return retUser;
    }
}
