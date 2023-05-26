package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ErrorsIO.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    @Autowired
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public Review addReview(Review review) {
        final String sql = "INSERT INTO FILMORATE_SHEMA.REVIEWS (ID_FILM, ID_USER, CONTENT, IS_POSITIVE)" +
                "Values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"ID_REVIEW"});
            stmt.setInt(1, review.getFilmId());
            stmt.setInt(2, review.getUserId());
            stmt.setString(3, review.getContent());
            stmt.setBoolean(4, review.getIsPositive());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        review.setReviewId(id);
        review.setUseful(0L);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE FILMORATE_SHEMA.REVIEWS SET CONTENT = ?," +
                "IS_POSITIVE = ? WHERE ID_REVIEW = ?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
        return jdbcTemplate.queryForObject("SELECT * FROM FILMORATE_SHEMA.REVIEWS WHERE ID_REVIEW = ?",
                this::mapRowToReview, review.getReviewId());
    }

    @Override
    public long deleteReviewById(long id) {
        String sql = "DELETE FROM FILMORATE_SHEMA.REVIEWS WHERE ID_REVIEW = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public Review findReviewById(long id) {
        String sql = "SELECT * FROM FILMORATE_SHEMA.REVIEWS WHERE ID_REVIEW = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToReview, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Отзыв с индификатором {} не найден.", id);
            throw new ItemNotFoundException("Отзыв не найден");
        }
    }

    @Override
    public List<Review> findAllReviews(int filmId, int count) {
        List<Review> allReviews = new ArrayList<>();
        String sql = "SELECT * FROM FILMORATE_SHEMA.REVIEWS WHERE ID_FILM = ? " +
                "ORDER BY USEFULL desc LIMIT ?";
        try {
            return jdbcTemplate.query(sql, this::mapRowToReview, filmId, count);
        } catch (EmptyResultDataAccessException e) {
            log.info("У фильма с индификатором {} нет отзывов.", filmId);
            throw new ItemNotFoundException("Отзыв не найден");
        }
    }

    @Override
    public List<Review> findReviews() {
        List<Review> allReviews = new ArrayList<>();
        String sql = "SELECT * FROM FILMORATE_SHEMA.REVIEWS " +
                "ORDER BY USEFULL";
        allReviews.addAll(jdbcTemplate.query(sql, this::mapRowToReview));
        return allReviews;
    }

    @Override
    public long likeReview(int userId, long id) {
        String sql = "UPDATE FILMORATE_SHEMA.REVIEWS SET USEFULL = USEFULL + 1 WHERE ID_REVIEW = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public long dislikeReview(int userId, long id) {
        String sql = "UPDATE FILMORATE_SHEMA.REVIEWS SET USEFULL = USEFULL - 1 WHERE ID_REVIEW = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public long deleteLikeReview(int userId, long id) {
        String sql = "UPDATE FILMORATE_SHEMA.REVIEWS SET USEFULL = USEFULL - 1 WHERE ID_REVIEW = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    @Override
    public long deleteDislikeReview(int userId, long id) {
        String sql = "UPDATE FILMORATE_SHEMA.REVIEWS SET USEFULL = USEFULL + 1 WHERE ID_REVIEW = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getLong("ID_REVIEW"))
                .filmId(resultSet.getInt("ID_FILM"))
                .userId(resultSet.getInt("ID_USER"))
                .content(resultSet.getString("CONTENT"))
                .isPositive(resultSet.getBoolean("IS_POSITIVE"))
                .useful(resultSet.getLong("USEFULL"))
                .build();
    }
}