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

@Data
@Slf4j
@Component
@Primary
public class DirectorDBStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;
    private DirectorDBStorage directorDBStorage;

    static final String sqlQueryCreateDirector = "INSERT INTO FILMORATE_SHEMA.DIRECTOR (NAME_DIRECTOR) VALUES (?)";

    @Autowired
    private DirectorDBStorage(JdbcTemplate jdbcTemplate, DirectorDBStorage directorDBStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.directorDBStorage = directorDBStorage;
    }

    @Override
    public List<Director> findDirectorsByIdFilm(int idFilm) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.DIRECTOR_LIST AS DL JOIN FILMORATE_SHEMA.DIRECTOR AS D ON DL.ID_DIRECTOR = D.ID_DIRECTOR WHERE D.ID_FILM = ?";
        List<Director> result = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToDirector(rs), idFilm).stream().toList();
        log.info("Запрос findGenreByIdFilm > {} -- {} ", sqlQuery, result);
        return result;
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
        log.info("Запрос getCollectionDirector > {}", sqlQuery);
        Collection<Director> listDirector = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToDirector(rs));
        log.info("Collection > {}", listDirector);
        return listDirector;
    }

    @Override
    public Director getByIdDirector(int idD) {
        Director director;
        log.info("Запрос getByIdDirector.");
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.DIRECTOR WHERE ID_DIRECTOR = ?";
        director = jdbcTemplate.query(sqlQuery, this::mapToDirector, idD);
        log.info("Запрос getByIdDirector > {} --> {} ", sqlQuery, director);
        return director;
    }

    @Override
    public Director update(Director director) {
        if (this.getIdExist(director.getId())) {
            String sqlQuery = "UPDATE FILMORATE_SHEMA.DIRECTOR SET NAME_DIRECTOR = ? WHERE ID_DIRECTOR = ?";
            log.info("Запрос update > {} --> {} ", sqlQuery, director);
            jdbcTemplate.update(sqlQuery, director.getName());
        } else {
            log.info("Запрос update > нет такого режисёра {}", director);
            throw new MethodArgumentNotException("Ну нет такого режисёра!");
        }
        return director;
    }

   @Override
    public boolean getIdExist(int idD) {
        String sqlQuery = "SELECT ID_DIRECTOR FROM FILMORATE_SHEMA.DIRECTOR WHERE ID_DIRECTOR = ?";
        log.info("Запрос getIdExist Director > {}", sqlQuery);
        SqlRowSet idRows = jdbcTemplate.queryForRowSet(sqlQuery, idD);
        return idRows.next();
    }




}
