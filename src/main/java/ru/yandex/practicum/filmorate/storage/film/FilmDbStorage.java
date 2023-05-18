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
    static final String sqlQueryCreate = "INSERT INTO FILMORATE_SHEMA.FILMS (ID_RATE, DURATION, RELEASE_DATE, DESCRIPTION, NAME_FILMS) VALUES (?,?,CAST (? AS DATE),?,?)";

    @Autowired
    private GenreDbStorage genreDbStorage;

    @Autowired
    private FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected Film mapToFilm(ResultSet rs, int rowNum) throws SQLException {
        log.info("Запрос mapToFilm ResultSet > {}", rs);
        List<Genre> genres = genreDbStorage.findGenreByIdFilm(rs.getInt("ID_FILM"));
        Set<Long> likesF = getLikes(rs.getInt("ID_FILM"));
        return Film.builder()
                .idFilm(rs.getInt("ID_FILM"))
                .idRate(rs.getInt("ID_RATE"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getLong("DURATION"))
                .nameFilm(rs.getString("NAME_FILMS"))
                .likes(likesF)
                .genre(genres)
                .build();
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("Запрос create > {}", sqlQueryCreate);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryCreate, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, film.getIdRate());
            stmt.setLong(2, film.getDuration());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setString(4, film.getDescription());
            stmt.setString(5, film.getNameFilm());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        film.setIdFilm(id);
        return film;
    }

    @Override
    public long getCollectionSize() {
        String sqlQuery = "SELECT COUNT(*) FROM FILMORATE_SHEMA.FILMS";
        log.info("Запрос getCollectionSize > {}", sqlQuery);
        return jdbcTemplate.queryForObject(sqlQuery, Long.class);
    }

    @Override
    public List<Film> getFilmsToRate(int idRate) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.FILMS WHERE ID_RATE = ?";
        log.info("Запрос getFilmsToRate > {}", sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::mapToFilm, idRate);
    }

    @Override
    public Collection<Film> getCollectionFilm() {
        String sqlQuery = "SELECT * FROM " +
                "(FILMORATE_SHEMA.FILMS AS F LEFT JOIN FILMORATE_SHEMA.RATE AS R ON F.ID_RATE = R.ID_RATE) " +
                "LEFT JOIN FILMORATE_SHEMA.LIKES_SET AS LS ON F.ID_FILM = LS.ID_FILM";
        log.info("Запрос getCollectionFilm > {}", sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::mapToFilm);
    }

    @Override
    public void deleteByIdFilm(int idFilm) {
        String sqlQuery = "DELETE FROM FILMORATE_SHEMA.FILMS WHERE ID_FILM = ?";
        log.info("Запрос deleteByIdFilm > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idFilm);
    }

    @Override
    public Collection<Film> getMaxPopular(int scoring) {
        String sqlQuery = "SELECT * " +
                "FROM (FILMORATE_SHEMA.FILMS AS F LEFT JOIN FILMORATE_SHEMA.RATE AS R ON F.ID_RATE = R.ID_RATE GROUP BY F.ID_FILM) " +
                "LEFT JOIN FILMORATE_SHEMA.LIKES_SET AS LS ON F.ID_FILM = LS.ID_FILM " +
                "ORDER BY COUNT(LS.ID_USER) DESC, F.ID_RATE LIMIT = ?";
        log.info("Запрос getMaxPopular > {}", sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::mapToFilm, scoring);
    }

    @Override
    public Film getByIdFilm(int idFilm) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.FILMS WHERE ID_FILM = ?";
        log.info("Запрос getByIdFilm > {}", sqlQuery);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapToFilm, idFilm);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMORATE_SHEMA.FILMS SET ID_RATE = ?, DURATION = ?, RELEASE_DATE = CAST (? AS DATE)," +
                "DESCRIPTION = ?, NAME_FILMS = ? WHERE ID_FILM = ?";
        log.info("Запрос update > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, film.getIdRate(), film.getDuration(),
                film.getReleaseDate(), film.getDescription(), film.getNameFilm(), film.getIdFilm());
        return film;
    }

    @Override
    public Set<Long> getLikes(int idFilm) {
        Set<Long> likes = new HashSet<>();
        String sqlQuery = "SELECT ID_USER FROM FILMORATE_SHEMA.LIKES_SET WHERE ID_FILM = ?";
        log.info("Запрос getLikes > {}", sqlQuery);
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(sqlQuery, idFilm);
        while (likeRows.next()) {
            likes.add(likeRows.getLong("id_user"));
        }
        return likes;
    }

    @Override
    public void addLike(int idFilm, int idUser) {
        String sqlQuery = "INSERT INTO FILMORATE_SHEMA.LIKES_SET (ID_FILM, ID_USER) VALUES (?, ?)";
        log.info("Запрос addLike > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idFilm, idUser);
    }

    @Override
    public void deleteLike(int idFilm, int idUser) {
        String sqlQuery = "DELETE FROM FILMORATE_SHEMA.LIKES_SET WHERE ID_FILM = ? AND ID_USER = ?";
        log.info("Запрос deleteLike > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idFilm, idUser);
    }
    @Override
    public boolean getLikeExist(int idFilm, int idUser) {
        String sqlQuery = "SELECT ID_USER = ? FROM FILMORATE_SHEMA.LIKES_SET WHERE ID_FILM = ?";
        log.info("Запрос getIdExist > {}", sqlQuery);
        SqlRowSet idRows = jdbcTemplate.queryForRowSet(sqlQuery, idUser, idFilm);
        return idRows.next();
    }
}