package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;


import java.sql.*;

import java.sql.Date;
import java.util.*;


@Data
@Slf4j
@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    static final String sqlQueryCreate = "INSERT INTO FILMS (ID_RATE, DURATION, RELEASE_DATE, DESCRIPTION, NAME) VALUES (?,?,?,?,?)";

    @Autowired
    private GenreDbStorage genreDbStorage;

    @Autowired
    private FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected Film mapToFilm(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genres = genreDbStorage.findByIdFilm(rs.getInt("ID_FILM"));
        Set<Long> likesF = getLikes(rs.getInt("ID_FILM"));
        return Film.builder()
                .idFilm(rs.getInt("ID_FILM"))
                .idRate(rs.getInt("ID_RATE"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getLong("DURATION"))
                .name(rs.getString("NAME"))
                .likes(likesF)
                .genre(genres)
                .build();
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("Запрос > {}", sqlQueryCreate);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryCreate, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(2, film.getIdRate());
            stmt.setLong(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setString(5, film.getDescription());
            stmt.setString(6, film.getName());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        film.setIdFilm(id);
        return film;
    }

    @Override
    public long getCollectionSize() {
        String sqlQuery = "SELECT COUNT(*) FROM FILMS";
        log.info("Запрос > {}", sqlQuery);
        return jdbcTemplate.queryForObject(sqlQuery, Long.class);
    }

    @Override
    public List<Film> getFilmsToRate(int idRate) {
        String sqlQuery = "SELECT * FROM FILMS WHERE ID_RATE = ?";
        log.info("Запрос > {}", sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::mapToFilm, idRate);
    }

    @Override
    public Collection<Film> getCollectionFilm() {
        String sqlQuery = "SELECT * FROM " +
                "(FILMS AS F LEFT JOIN RATE AS R ON F.ID_RATE = R.ID_RATE) " +
                "LEFT JOIN LIKES_SET AS LS ON F.ID_FILM = LS.ID_FILM";
        log.info("Запрос > {}", sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::mapToFilm);
    }

    @Override
    public void deleteByIdFilm(int idFilm) {
        String sqlQuery = "DELETE FROM FILMS WHERE ID_FILM = ?";
        log.info("Запрос > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idFilm);
    }

    @Override
    public Collection<Film> getMaxPopular(int scoring) {
        String sqlQuery = "SELECT * " +
                //"F.ID_FILM, F.ID_RATE, F.DURATION, F.RELEASE_DATE, F.DESCRIPTION, F.NAME, " +
                //"R.RATE_DATE, LS.ID_USER " +
                "FROM (FILMS AS F LEFT JOIN RATE AS R ON F.ID_RATE = R.ID_RATE GROUP BY F.ID_FILM) " +
                "LEFT JOIN LIKES_SET AS LS ON F.ID_FILM = LS.ID_FILM " +
                "ORDER BY COUNT(LS.ID_USER) DESC, F.ID_RATE LIMIT = ?";
        log.info("Запрос > {}", sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::mapToFilm, scoring);
    }

    @Override
    public Film getByIdFilm(int idFilm) {
        String sqlQuery = "SELECT * FROM FILMS WHERE ID_FILM = ?";
        log.info("Запрос > {}", sqlQuery);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapToFilm, idFilm);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET ID_FILM = ?, ID_RATE = ?, DURATION = ?, " +
                "RELEASE_DATE = ?, DESCRIPTION = ?, NAME = ? ";
        log.info("Запрос > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, film.getIdFilm(), film.getIdRate(), film.getDuration(),
                film.getReleaseDate(), film.getDescription(), film.getName());
        return film;
    }

    @Override
    public Set<Long> getLikes(int idFilm) {
        Set<Long> likes = new HashSet<>();
        String sqlQuery = "SELECT ID_USER FROM LIKES_SET WHERE ID_FILM = ?";
        log.info("Запрос > {}", sqlQuery);
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(sqlQuery, idFilm);
        while (likeRows.next()) {
            likes.add(likeRows.getLong("user_id"));
        }
        return likes;
    }

    @Override
    public void addLike(int idFilm, int idUser) {
        String sqlQuery = "INSERT INTO LIKES_SET (ID_FILM, ID_USER) VALUES (?, ?)";
        log.info("Запрос > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idFilm, idUser);
    }

    @Override
    public void deleteLike(int idFilm, int idUser) {
        String sqlQuery = "DELETE FROM FILMS_LIKES WHERE ID_FILM = ? AND ID_USER = ?";
        log.info("Запрос > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idFilm, idUser);
    }
    @Override
    public boolean getLikeExist(int idFilm, int idUser) {
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("SELECT * FROM LIKES_SET WHERE ID_FILM = ? AND ID_USER = ?", idFilm, idUser);
        return idRows.next();
    }
}