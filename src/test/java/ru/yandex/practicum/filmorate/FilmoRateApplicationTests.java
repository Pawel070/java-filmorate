package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.RateStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final GenreStorage genreStorage;
    private final RateStorage rateStorage;
    private final FriendStorage friendStorage;
    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;

    User user1;
    User user2;
    Film film1;
    Film film2;

    @BeforeEach
    public void setup() {
         Set<Long> like = new HashSet<>();
        List<Genre> genres = new ArrayList<>();
        user1 = User.builder()
                .idUser(1)
                .name("Имя1")
                .birthday(LocalDate.of(1995, 12, 28))
                .email("test@qq1.ru")
                .login("Логин1")
                .build();

        user2 = User.builder()
                .idUser(2)
                .name("Имя2")
                .birthday(LocalDate.of(2000, 12, 28))
                .email("test@qq2.ru")
                .login("Логин2")
                .build();

        film1 = Film.builder()
                .idFilm(1)
                .name("Фильм1")
                .description("Описание1")
                .releaseDate(LocalDate.of(1995, 12, 28))
                .duration(50)
                .likes(like)
                .genre(genres)
                .idRate(1)
                .build();

        film2 = Film.builder()
                .idFilm(2)
                .name("Фильм2")
                .description("Описание2")
                .releaseDate(LocalDate.of(2000, 12, 28))
                .duration(100)
                .likes(like)
                .genre(genres)
                .idRate(2)
                .build();
    }

    @AfterEach
    void cleanDb() {
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM friends");
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("DELETE FROM film_genre");
        jdbcTemplate.update("DELETE FROM liked_by");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE films ALTER COLUMN id RESTART WITH 1");
    }


    @Test
    public void createAndFindUserTest() {
        userStorage.create(user1);
        assertEquals(userService.findUserById(1).getName(), "Имя");
    }

    @Test
    public void FindAllUsersTest() {
        userStorage.create(user1);
        userStorage.create(user2);
        assertEquals(userStorage.getAllUser().size(), 2);
    }

    @Test
    public void updateUserTest() {
        userStorage.create(user1);
        user1.setName("newName");
        userStorage.update(user1);
        assertEquals(userStorage.getByIdUser(1).getName(), "newName");
    }

    @Test
    public void isIdExistUserTest() {
        userStorage.create(user1);
        assertTrue(userStorage.getIdExist(user1.getIdUser()));
        assertFalse(userStorage.getIdExist(9999));
    }


    @Test
    public void addAndRemoveFriendTest() {
        userStorage.create(user1);
        userStorage.create(user2);
        friendStorage.setRequestsFriends(user1.getIdUser(), user2.getIdUser());
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("SELECT * FROM friends WHERE user_id = ? AND friend_id = ?", user1.getIdUser(), user2.getIdUser());
        assertTrue(idRows.next());
        friendStorage.deleteFriend(user1.getIdUser(), user2.getIdUser());
        idRows = jdbcTemplate.queryForRowSet("SELECT * FROM friends WHERE user_id = ? AND friend_id = ?", user1.getIdUser(), user2.getIdUser());
        assertFalse(idRows.next());
    }

    @Test
    public void findFriendsOfUserTest() {
        userStorage.create(user1);
        userStorage.create(user2);
        friendStorage.setRequestsFriends(user1.getIdUser(), user2.getIdUser());
        assertEquals(friendStorage.getFriendsUser(user1.getIdUser()).get(0), userStorage.getByIdUser(user2.getIdUser()));
    }

    @Test
    public void findCommonFriendsTest() {
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user2);
        friendStorage.setRequestsFriends(2, 1);
        friendStorage.setRequestsFriends(3, 1);
        assertEquals(friendStorage.getCommonFriends(2, 3).get(0), userStorage.getByIdUser(1));
    }


    @Test
    public void createAndFindFilmTest() {
        filmDbStorage.create(film1);
        assertEquals(filmDbStorage.getByIdFilm(film1.getIdFilm()).getName(), "Фильм");
    }

    @Test
    public void findAllFilmsTest() {
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        assertEquals(filmDbStorage.getCollectionFilm().size(), 2);
        assertEquals(filmDbStorage.getCollectionFilm().stream().toList().get(0), filmDbStorage.getByIdFilm(film1.getIdFilm()));
    }

    @Test
    public void updateFindFilmTest() {
        filmDbStorage.create(film1);
        film1.setName("newNameForTest");
        filmDbStorage.update(film1);
        assertEquals(filmDbStorage.getByIdFilm(user1.getIdUser()).getName(), "newNameForTest");
    }

    @Test
    public void likeAndRemoveFilmTest() {
        filmDbStorage.create(film1);
        userStorage.create(user1);
        filmDbStorage.addLike(1, 1);
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("SELECT * FROM liked_by WHERE film_id = ? AND user_id = ?", film1.getIdFilm(), user1.getIdUser());
        assertTrue(idRows.next());
        assertEquals(idRows.getLong("film_id"), 1);
        assertEquals(idRows.getLong("user_id"), 1);
        filmDbStorage.getLikes(1);
        idRows = jdbcTemplate.queryForRowSet("SELECT * FROM liked_by WHERE film_id = ? AND user_id = ?", film1.getIdFilm(), user1.getIdUser());
        assertFalse(idRows.next());
    }

    @Test
    public void findByIdGenreTest() {
        film1.getGenre().stream().toList().get(0);
        assertEquals(genreStorage.findByIdFilm(1).get(1), "Комедия");
    }

    @Test
    public void findByIdRateTest() {
        assertEquals(rateStorage.checkRate(2).toString(), "PG");
    }

    @Test
    public void findAllRateTest() {
        assertEquals(rateStorage.getRate().size(), 5);
    }

    @Test
    public void likeFilmAndGetLikedByOfFilmTest() {
        userStorage.create(user1);
        filmDbStorage.create(film1);
        filmDbStorage.addLike(1, 1);
        assertEquals(filmDbStorage.getLikes(1).size(), 1);
    }

    @Test
    public void isLikeExistLikedByTest() {
        userStorage.create(user1);
        filmDbStorage.create(film1);
        filmDbStorage.addLike(1, 1);
        assertTrue(filmDbStorage.getLikeExist(1, 1));
    }

    @Test
    public void removeLikeLikedByTest() {
        userStorage.create(user1);
        filmDbStorage.create(film1);
        filmDbStorage.addLike(1, 1);
        assertEquals(filmDbStorage.getLikes(1).size(), 1);
        filmDbStorage.deleteLike(1, 1);
        assertEquals(filmDbStorage.getLikes(1).size(), 0);

    }
}
