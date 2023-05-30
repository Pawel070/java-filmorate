package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewIntegrationTest {

    private final ReviewDbStorage reviewDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    public Film film1;
    public Film film2;

    public User user1;

    public User user2;

    public Review review1;

    public Review review2;

    public Review updateReview;

    @BeforeEach
    void start() {
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


        review1 = Review.builder()
                .userId(1)
                .filmId(1)
                .content("Good")
                .isPositive(true)
                .build();

        review2 = Review.builder()
                .userId(1)
                .filmId(1)
                .content("Bad")
                .isPositive(false)
                .build();

        updateReview = Review.builder()
                .reviewId(1L)
                .userId(2)
                .filmId(1)
                .content("Bad")
                .isPositive(false)
                .build();
        filmDbStorage.create(film1);
        userDbStorage.create(user1);
        userDbStorage.create(user2);
    }

    @AfterEach
    void resetAll() {
        jdbcTemplate.update("ALTER TABLE FILMORATE_SHEMA.REVIEWS " +
                "ALTER COLUMN ID_REVIEW RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM FILMORATE_SHEMA.REVIEWS");
        jdbcTemplate.update("ALTER TABLE FILMORATE_SHEMA.FILMS " +
                "ALTER COLUMN ID_FILM RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM FILMORATE_SHEMA.FILMS");
        jdbcTemplate.update("ALTER TABLE FILMORATE_SHEMA.USERS " +
                "ALTER COLUMN ID_USER RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM FILMORATE_SHEMA.USERS");
    }

    @Test
    public void addReviewTest() {
        Review ans = reviewDbStorage.addReview(review1);
        assertEquals(ans.getReviewId(), 1L);
        assertEquals(ans.getUserId(), 1);
        assertEquals(ans.getFilmId(), 1);
        assertEquals(ans.getContent(), "Good");
        assertEquals(ans.getIsPositive(), true);
    }

    @Test
    public void updateReviewTest() {
        reviewDbStorage.addReview(review1);
        Review ans = reviewDbStorage.updateReview(updateReview);
        assertEquals(ans.getContent(), "Bad");
        assertEquals(ans.getIsPositive(), false);
    }

    @Test
    public void deleteReviewTest() {
        reviewDbStorage.addReview(review1);
        reviewDbStorage.deleteReviewById(1L);
        List<Review> ans = reviewDbStorage.findAllReviews(1, 5);
        assertEquals(ans.size(), 0);
    }

    @Test
    public void findReviewByIdTest() {
        reviewDbStorage.addReview(review1);
        Review ans = reviewDbStorage.findReviewById(1L);
        assertEquals(ans.getReviewId(), 1L);
        assertEquals(ans.getUserId(), 1);
        assertEquals(ans.getFilmId(), 1);
        assertEquals(ans.getContent(), "Good");
        assertEquals(ans.getIsPositive(), true);
    }

    @Test
    public void findAllReviewsTest() {
        reviewDbStorage.addReview(review1);
        reviewDbStorage.addReview(review2);
        List<Review> ans = reviewDbStorage.findAllReviews(1, 1);
        assertEquals(ans.size(), 1);
    }

    @Test
    public void likeReviewTest() {
        reviewDbStorage.addReview(review1);
        reviewDbStorage.likeReview(1, 1);
        assertEquals(reviewDbStorage.findReviewById(1).getUseful(), 1);
    }

    @Test
    public void dislikeReviewTest() {
        reviewDbStorage.addReview(review1);
        reviewDbStorage.dislikeReview(1, 1);
        assertEquals(reviewDbStorage.findReviewById(1).getUseful(), -1);
    }

    @Test
    public void deleteLikeReviewTest() {
        reviewDbStorage.addReview(review1);
        reviewDbStorage.likeReview(1, 1);
        reviewDbStorage.deleteLikeReview(1, 1);
        assertEquals(reviewDbStorage.findReviewById(1).getUseful(), 0);
    }

    @Test
    public void deleteDislikeReviewTest() {
        reviewDbStorage.addReview(review1);
        reviewDbStorage.dislikeReview(1, 1);
        reviewDbStorage.deleteDislikeReview(1, 1);
        assertEquals(reviewDbStorage.findReviewById(1).getUseful(), 0);
    }
}