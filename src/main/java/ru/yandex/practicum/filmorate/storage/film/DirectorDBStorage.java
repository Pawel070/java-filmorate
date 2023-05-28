package ru.yandex.practicum.filmorate.storage.film;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

@Data
@Slf4j
@Component
@Primary
public class DirectorDBStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    static final String sqlQueryCreateDirector = "INSERT INTO FILMORATE_SHEMA.DIRECTOR (NAME_DIRECTOR) VALUES (?)";

    @Autowired
    private FilmDbStorage filmDbStorage;

    @Autowired
    private DirectorDBStorage(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
    }

    protected Director mapToDirector(ResultSet rs) throws SQLException {
        log.info("Запрос mapToDirector ResultSet > {}", rs);
        return Director.builder()
                .id(rs.getInt("ID_DIRECTOR"))
                .name(rs.getString("NAME_DIRECTOR"))
                .build();
    }

    @Override
    public Director create(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("Запрос create > {} --> {}", director, sqlQueryCreateDirector);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryCreateDirector, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, director.getId());
            stmt.setString(2, director.getName());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        director.setId(id);
        log.info("Результат create режисёр > {} ", director);
        return director;
    }

    @Override
    public long getCollectionSize() {
        String sqlQuery = "SELECT COUNT(*) FROM FILMORATE_SHEMA.DIRECTOR";
        log.info("Запрос getCollectionSize > {}", sqlQuery);
        return jdbcTemplate.queryForObject(sqlQuery, Long.class);
    }

    @Override
    public void deleteByIdDirector(int idD) {
        String sqlQuery = "DELETE FROM FILMORATE_SHEMA.DIRECTOR WHERE ID_DIRECTOR = ?";
        log.info("Запрос deleteByIdDirector > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idD);
    }

    @Override
    public Collection<Director> getCollectionDirector() {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.DIRECTOR";
        log.info("Запрос getCollectionFilm > {}", sqlQuery);
        Collection<Director> listDirector = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToDirector(rs));
        log.info("Collection > {}", listDirector);
        return listDirector;
    }

    @Override
    public Director getByIdDirector(int idD) {
        Director director;
        log.info("Запрос getByIdFilm.");
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.DIRECTOR WHERE ID_DIRECTOR = ?";
        director = jdbcTemplate.query(sqlQuery, this::mapToDirector, idD);
        log.info("Запрос getByIdFilm > {} --> {} ", sqlQuery, director);
        return director;
    }

    @Override
    public Director update(Director director) {
        if (this.getIdExist(director.getId())) {
            String sqlQuery = "UPDATE FILMORATE_SHEMA.DIRECTOR SET NAME_DIRECTOR = ? WHERE ID_DIRECTOR = ?";
            log.info("Запрос update > {} --> {} ", sqlQuery, director);
            jdbcTemplate.update(sqlQuery, director.getName());
        } else {
            log.info("Запрос update > нет такого фильма {}", director);
            throw new MethodArgumentNotException("Ну нет такого фильма!");
        }
        return director;
    }

    @Override
    public List<Film> getFilmsByDirector(int directorId, String sorting) {
       Director director = getByIdDirector(directorId);
       List<Film> films;
        if (sorting.equals("year")) {
            String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.FILMS AS F " +
                              "WHERE F.ID_DIRECTOR  = ? ORDER BY F.RELEASE_DATA";
            films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmDbStorage.mapToFilm(rs, rowNum), directorId);
        } else {
            String sqlQuery = "SELECT COUNT(LS.ID_USER) FROM FILMORATE_SHEMA.LIKES_SET AS LS " +
                    "WHERE (SELECT F.ID_FILM FROM FILMORATE_SHEMA.FILMS AS F " +
                    "WHERE F.ID_DIRECTOR  = ?) GROUP BY LS.ID_USER ORDER BY LS.ID_USER DESC";
            films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmDbStorage.mapToFilm(rs, rowNum), directorId);
        }
        return films;
    }

   @Override
    public boolean getIdExist(int idD) {
        String sqlQuery = "SELECT ID_DIRECTOR FROM FILMORATE_SHEMA.DIRECTOR WHERE ID_DIRECTOR = ?";
        log.info("Запрос getIdExist Director > {}", sqlQuery);
        SqlRowSet idRows = jdbcTemplate.queryForRowSet(sqlQuery, idD);
        return idRows.next();
    }




}