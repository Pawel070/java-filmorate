package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.RateStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
        @Sql("/schema.sql"),
        @Sql("/data.sql"),
//        @Sql("/setUpBase.sql")
})

public class FilmoRateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final GenreStorage genreStorage;
    private final RateStorage rateStorage;
    private final FriendStorage friendStorage;
    private final UserService userService;
    private static JdbcTemplate jdbcTemplate;

    User user1;
    User user2;
    User user3;
    Film film1;
    Film film2;

    @BeforeEach
    public void setup() {

        Set<Long> like = new HashSet<>();
        List<Genre> genres = new ArrayList<>();
        user1 = User.builder()
                .id(1)
                .name("Имя1")
                .birthday(LocalDate.of(1995, 12, 28))
                .email("test@qq1.ru")
                .login("Логин1")
                .build();

        user2 = User.builder()
                .id(2)
                .name("Имя2")
                .birthday(LocalDate.of(2000, 12, 28))
                .email("test@qq2.ru")
                .login("Логин2")
                .build();

         user3 = User.builder()
                .id(3)
                .name("Имя3")
                .birthday(LocalDate.of(2003, 12, 28))
                .email("test@qq3.ru")
                .login("Логин3")
                .build();

        Rating rating = new Rating(1, "");

        film1 = Film.builder()
                .id(1)
                .name("Фильм1")
                .description("Описание1")
                .releaseDate(LocalDate.of(1995, 12, 28))
                .duration(50)
                .likes(like)
                .genres(genres)
                .mpa(rating)
                .build();

        film2 = Film.builder()
                .id(2)
                .name("Фильм2")
                .description("Описание2")
                .releaseDate(LocalDate.of(2000, 12, 28))
                .duration(100)
                .likes(like)
                .genres(genres)
                .mpa(rating)
                .build();
    }

    @Test
    public void createAndFindUserTest() {
        userStorage.create(user1);
        assertEquals(userService.findUserById(user1.getId()).getName(), "Имя1");
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
        assertEquals(userStorage.getByIdUser(user1.getId()).getName(), "newName");
    }

    @Test
    public void isIdExistUserTest() {
        userStorage.create(user1);
        assertTrue(userStorage.getIdExist(user1.getId()));
        assertFalse(userStorage.getIdExist(9999));
    }

    @Test
    public void addAndRemoveFriendTest() {
        userStorage.create(user1);
        userStorage.create(user2);
        friendStorage.setRequestsFriends(user1.getId(), user2.getId());
        log.info("TEST Запрос addAndRemoveFriendTest getFriendsRequests > {} ok.", friendStorage.getFriendsRequests(user1.getId()));
        assertEquals(friendStorage.getFriendsRequests(user1.getId()).size(), 1);
        friendStorage.deleteFriend(user1.getId(), user2.getId());
        assertEquals(friendStorage.getFriendsRequests(user1.getId()).size(), 0);
    }

    @Test
    public void findCommonFriendsTest() {
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        friendStorage.setRequestsFriends(user2.getId(), user1.getId());
        friendStorage.setRequestsFriends(user3.getId(), user1.getId());
        assertEquals(friendStorage.getCommonFriends(user2.getId(), user3.getId()).get(0), userStorage.getByIdUser(user1.getId()));
    }

    @Test
    public void createAndFindFilmTest() {
        filmDbStorage.create(film1);
        assertEquals(filmDbStorage.getByIdFilm(film1.getId()).getName(), "Фильм1");
    }

    @Test
    public void findAllFilmsTest() {
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        assertEquals(filmDbStorage.getCollectionFilm().size(), 2);
    }

    @Test
    public void updateFindFilmTest() {
        filmDbStorage.create(film1);
        film1.setName("newNameForTest");
        filmDbStorage.update(film1);
        assertEquals(filmDbStorage.getByIdFilm(film1.getId()).getName(), "newNameForTest");
    }

    @Test
    public void addAndRemoveLikeTest() {
        filmDbStorage.create(film1);
        userStorage.create(user1);
        filmDbStorage.addLike(film1.getId(), user1.getId());
        Set<Long> likes = filmDbStorage.getLikes(film1.getId());
        log.info("TEST Запрос addAndRemoveLikeTest add likes > {} ", likes);
        assertEquals(likes.size(), 1);
        filmDbStorage.deleteLike(film1.getId(), user1.getId());
        likes = filmDbStorage.getLikes(film1.getId());
        log.info("TEST Запрос addAndRemoveLikeTest delete likes > {} ", likes);
        assertEquals(likes.size(), 0);
    }

    @Test
    public void findByIdGenreTest() {
        log.info("TEST Запрос findByIdGenreTest getGenres > {} ", genreStorage.getGenres());
        filmDbStorage.create(film1);
        List<Genre> genres = genreStorage.findGenreByIdFilm(film1.getId());
        log.info("TEST Запрос findByIdGenreTest > {} у {}", genres.size(), film1);
        assertEquals(genres.size(), 0);
    }

    @Test
    public void findByIdRateTest() {
        log.info("TEST Запрос findByIdRateTest > ok.");
        assertEquals(rateStorage.checkRate(1).getName(), "G");
    }

    @Test
    public void findAllRateTest() {
        assertEquals(rateStorage.getRate().size(), 5);
    }

    @Test
    public void likeFilmAndGetLikedByOfFilmTest() {
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        filmDbStorage.addLike(film1.getId(), user1.getId());
        filmDbStorage.addLike(film1.getId(), user2.getId());
        filmDbStorage.addLike(film2.getId(), user2.getId());
        filmDbStorage.addLike(film2.getId(), user3.getId());
        film1.setLikes(filmDbStorage.getLikes(film1.getId()));
        film2.setLikes(filmDbStorage.getLikes(film2.getId()));
        log.info("TEST Запрос likeFilmAndGetLikedByOfFilmTest > 1 - {} , 2 - {} , ok.", film1.getLikes(), film2.getLikes());
        assertEquals(filmDbStorage.getLikes(film1.getId()).size(), 2);
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
        filmDbStorage.addLike(film1.getId(), user1.getId());
        assertEquals(filmDbStorage.getLikes(film1.getId()).size(), 1);
        filmDbStorage.deleteLike(film1.getId(), user1.getId());
        assertEquals(filmDbStorage.getLikes(film1.getId()).size(), 0);

    }

    @Test
    public void getCollectionFilmTest() {
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        Collection<Film> listFilm = filmDbStorage.getCollectionFilm();
        log.info("TEST Запрос getCollectionFilmTest > {} ", listFilm);
        assertEquals(listFilm.size(), 2);
    }

}
