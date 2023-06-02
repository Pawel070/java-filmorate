package ru.yandex.practicum.filmorate.storage.film;

import java.sql.*;
import java.util.*;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.model.Director;

@Data
@Slf4j
@Component
@Primary
public class DirectorDBStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    static final String sqlQueryCreateDirector = "INSERT INTO FILMORATE_SHEMA.DIRECTOR_LIST (NAME_DIRECTOR) VALUES ( ? )";

    @Autowired
    private DirectorDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> findDirectorsByIdFilm(int idFilm) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.DIRECTOR_LIST AS DL JOIN FILMORATE_SHEMA.DIRECTOR AS D ON DL.ID_DIRECTOR = D.ID_DIRECTOR WHERE D.ID_FILM = ?";
        List<Director> result = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToDirector(rs, rowNum), idFilm).stream().toList();
        log.info("Запрос findGenreByIdFilm > {} -- {} ", sqlQuery, result);
        return result;
    }

    protected Director mapToDirector(ResultSet rs, int rowNum) throws SQLException {
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
            stmt.setString(1, director.getName());
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
        log.info("Запрос getCollectionDirector > {}", sqlQuery);
        Collection<Director> listDirector = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToDirector(rs, rowNum));
        log.info("Collection > {}", listDirector);
        return listDirector;
    }

    @Override
    public Director getByIdDirector(int idD) {
        Director director;
        log.info("Запрос getByIdDirector {} ", idD);
        String sqlQuery;
        try {
            sqlQuery = "SELECT * FROM FILMORATE_SHEMA.DIRECTOR_LIST WHERE ID_DIRECTOR = ?";
            director = jdbcTemplate.queryForObject(sqlQuery, this::mapToDirector, idD);
        } catch (Exception e) {
            throw new MethodArgumentNotException("Ну нет такого режисёра !");
        }
        log.info("Запрос getByIdDirector > {} --> {} ", sqlQuery, director);
        return director;
    }

    @Override
    public Director update(Director director) {
        String sqlQuery;
        try {
            sqlQuery = "UPDATE FILMORATE_SHEMA.DIRECTOR SET NAME_DIRECTOR = ? WHERE ID_DIRECTOR = ?";
            jdbcTemplate.update(sqlQuery, director.getName());
            log.info("Запрос update > {} --> {} ", sqlQuery, director);
        } catch (Exception e) {
            log.info("Запрос update > нет такого режисёра {}", director);
            throw new MethodArgumentNotException("Ну нет такого режисёра!");
        }
        return director;
    }

    @Override
    public Director checkDirector(int idD) {
        Director director;
        try {
            String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.DIRECTOR_LIST WHERE ID = ?";
            director = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> mapToDirector(rs, rowNum), idD);
            log.info("Запрос checkGenre > {} -- {} ", sqlQuery, director);
        } catch (Exception e) {
            log.info("Жанра с id {} нет", idD);
            throw new MethodArgumentNotException("Жанра с id " + idD + " нет.");
        }
        return director;
    }


}
