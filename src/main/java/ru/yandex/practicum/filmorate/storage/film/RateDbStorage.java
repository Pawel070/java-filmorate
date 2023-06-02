package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.ErrorsIO.ItemNotFoundException;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;


@Data
@Component
@Slf4j
public class RateDbStorage implements RateStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RateDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Rating checkRate(int idRate) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.RATE WHERE ID_RATE = ?";
        Rating rating = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> mapToRate(rs), idRate);
        log.info("Запрос checkRate > {} - {} ", sqlQuery, rating);
        return rating;
    }

    @Override
    public Collection<Rating> getRate() {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.RATE";
        log.info("Запрос getRate > {}", sqlQuery);
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery);
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToRate(rs));
    }

    protected Rating mapToRate(ResultSet rs) throws SQLException {
        log.info("Запрос mapToRate ResultSet > {}", rs);
        return Rating.builder()
                .id(rs.getInt("ID_RATE"))
                .name(rs.getString("NAME"))
                .build();
    }

}
