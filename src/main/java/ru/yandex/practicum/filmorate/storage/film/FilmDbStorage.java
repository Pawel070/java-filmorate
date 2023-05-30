package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;


import java.sql.*;

import java.sql.Date;
import java.util.*;


@Data
@Slf4j
@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    private final DirectorStorage directorStorage;

    static final String sqlQueryCreate = "INSERT INTO FILMORATE_SHEMA.FILMS (ID_RATE, DURATION, RELEASE_DATE, DESCRIPTION, NAME_FILMS) VALUES (?,?,CAST (? AS DATE),?,?)";

    @Autowired
    private FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, DirectorStorage directorStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.directorStorage = directorStorage;
    }

    public Film mapToFilm(ResultSet rs, int rowNum) throws SQLException {
        log.info("Запрос mapToFilm ResultSet > {}", rs);
        Rating rating = new Rating(rs.getInt("ID_RATE"), "");
        List<Genre> genres = genreDbStorage.findGenreByIdFilm(rs.getInt("ID_FILM"));
        List<Director> directors = directorStorage.findDirectorsByIdFilm(rs.getInt("ID_FILM"));
        Set<Long> likesF = getLikes(rs.getInt("ID_FILM"));
        return Film.builder()
                .id(rs.getInt("ID_FILM"))
                .mpa(rating)
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getLong("DURATION"))
                .name(rs.getString("NAME_FILMS"))
                .likes(likesF)
                .genres(genres)
                .directors(directors)
                .build();
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("Запрос create > {} --> {}", film, sqlQueryCreate);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryCreate, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, film.getMpa().getId());
            stmt.setLong(2, film.getDuration());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setString(4, film.getDescription());
            stmt.setString(5, film.getName());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        film.setId(id);
        log.info("Результат create film > {} ", film);
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
                "LEFT JOIN FILMORATE_SHEMA.LIKES_SET AS LS ON F.ID_FILM = LS.ID_FILM " +
                "LEFT JOIN FILMORATE_SHEMA.GENRE_SET AS GS ON F.ID_FILM = GS.ID_FILM " +
                "LEFT JOIN FILMORATE_SHEMA.GENRE AS G ON GS.ID = G.ID  " +
                "LEFT JOIN FILMORATE_SHEMA.DIRECTOR AS D ON F.ID_FILM = D.ID_FILM ";
        log.info("Запрос getCollectionFilm > {}", sqlQuery);
        Collection<Film> listFilm = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToFilm(rs, rowNum));
        log.info("Collection > {}", listFilm);
        return listFilm;
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
        Collection<Film> listFilm = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToFilm(rs, rowNum), scoring);
        log.info("Collection > {}", listFilm);
        return listFilm;
    }

    @Override
    public Film getByIdFilm(int idFilm) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.FILMS AS F " +
                "LEFT JOIN FILMORATE_SHEMA.RATE AS R ON F.ID_RATE = R.ID_RATE " +
                "LEFT JOIN FILMORATE_SHEMA.GENRE_SET AS GS ON F.ID_FILM = GS.ID_FILM "+
                "LEFT JOIN FILMORATE_SHEMA.GENRE AS G ON GS.ID = G.ID  " +
                "LEFT JOIN FILMORATE_SHEMA.DIRECTOR AS D ON F.ID_FILM = D.ID_FILM " +
                "WHERE F.ID_FILM = ?";
        Film film;
        log.info("Запрос getByIdFilm {} -- {} ", sqlQuery, idFilm);
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> mapToFilm(rs, rowNum), idFilm);
        } catch (Exception e) {
            throw new MethodArgumentNotException("Ну нет такого фильма!");
        }
        log.info("Результат: {} ", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery;
        try {
            sqlQuery = "UPDATE FILMORATE_SHEMA.FILMS SET ID_RATE = ?, DURATION = ?, RELEASE_DATE = CAST (? AS DATE)," +
                    "DESCRIPTION = ?, NAME_FILMS = ? , Id_DIRECTOR = ? WHERE ID_FILM = ?";
            jdbcTemplate.update(sqlQuery, film.getMpa().getId(), film.getDuration(),
                    film.getReleaseDate(), film.getDescription(), film.getName(), film.getIdD(), film.getId());
            log.info("Запрос update > {} --> {} ", sqlQuery, getByIdFilm(film.getId()));
        } catch (Exception e) {
            log.info("Запрос update > нет такого фильма {}", film);
            throw new MethodArgumentNotException("Ну нет такого фильма!");
        }
        return film;
    }

    @Override
    public List<Film> getFilmsByDirector(int directorId, String sorting) {
       Director director = directorStorage.getByIdDirector(directorId);
       List<Film> films;
        if (sorting.equals("year")) {
            String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.FILMS AS F " +
                              "WHERE F.ID_DIRECTOR  = ? ORDER BY F.RELEASE_DATA";
            films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToFilm(rs, rowNum), directorId);
        } else {
            String sqlQuery = "SELECT COUNT(LS.ID_USER) FROM FILMORATE_SHEMA.LIKES_SET AS LS " +
                    "WHERE (SELECT F.ID_FILM FROM FILMORATE_SHEMA.FILMS AS F " +
                    "WHERE F.ID_DIRECTOR  = ?) GROUP BY LS.ID_USER ORDER BY LS.ID_USER DESC";
            films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToFilm(rs, rowNum), directorId);
        }
        return films;
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

    @Override
    public Optional<List<Film>> searchByAll(String query) {
        final String sqlQuery = "SELECT N.* FROM FILMORATE_SHEMA.FILMS AS N " +
                "LEFT JOIN FILMORATE_SHEMA.DIRECTOR AS D ON N.ID_DIRECTOR = D.ID_DIRECTOR " +
                "LEFT JOIN FILMORATE_SHEMA.RATE AS R ON N.ID_RATE = R.ID_RATE " +
                "WHERE D.NAME_DIRECTOR LIKE '%?%' OR N.NAME_FILMS LIKE '%?%' " +
                "ORDER BY R.RATE";
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, this::mapToFilm, query));
    }

    @Override
    public Optional<List<Film>> searchByName(String nameQuery) {
        final String sqlQuery = "SELECT F.NAME_FILMS FROM FILMORATE_SHEMA.FILMS AS F " +
             //   "(SELECT NAME_FILMS FROM FILMORATE_SHEMA.FILMS WHERE NAME_FILMS LIKE '%?%') " +
                "LEFT JOIN FILMORATE_SHEMA.RATE AS R ON F.ID_RATE = R.ID_FILM " +
                "WHERE F.NAME_FILMS LIKE '%?%' " +
                "ORDER BY R.RATE";
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, this::mapToFilm, nameQuery));
    }

    @Override
    public Optional<List<Film>> searchByDirector(String directorQuery) {
        final String sqlQuery = "SELECT N.* FROM FILMORATE_SHEMA.FILMS AS N " +
                "LEFT JOIN FILMORATE_SHEMA.DIRECTOR AS D ON N.ID_DIRECTOR = D.ID_DIRECTOR " +
                "LEFT JOIN FILMORATE_SHEMA.RATE AS R ON N.ID_RATE = R.ID_RATE " +
                "WHERE D.NAME_DIRECTOR LIKE '%?%' " +
                "ORDER BY R.RATE";
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, this::mapToFilm));
    }
    @Override
    public List<Film> getCommonFilms(int idUser) {
        String sql = "SELECT ID_FILM, NAME_FILMS, DESCRIPTION, RELEASE_DATE, DURATION, rating, ID_RATE, likesF" +
                "FROM FILM " +
                "LEFT JOIN likesF ON ID_FILM " +
                "WHERE ID_FILM = ? ";
        return jdbcTemplate.query(sql, this::mapToFilm, idUser);
    }
}