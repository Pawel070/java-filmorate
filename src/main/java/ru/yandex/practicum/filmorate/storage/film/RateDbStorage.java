package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.ErrorsIO.ItemNotFoundException;
import ru.yandex.practicum.filmorate.dataService.RateData;
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
        try {
            String sqlQuery = "SELECT * FROM RATE WHERE ID_RATE = ?";
            log.info("Запрос > {}", sqlQuery);
            return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> mapToRate(rs), idRate);
        } catch (Exception e) {
            log.info("Рейтинга с id {} нет", idRate);
            throw new ItemNotFoundException("Жанра с id " + idRate + " нет.");
        }
    }

    @Override
    public Collection<Rating> getRate() {
        String sqlQuery = "SELECT * FROM RATE";
        log.info("Запрос > {}", sqlQuery);
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery);
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToRate(rs));
    }

    protected Rating mapToRate(ResultSet rs) throws SQLException {
        return Rating.builder()
                .idRate(rs.getInt("ID_RATE"))
                .idRateDate(RateData.valueOf(rs.getString("RATE_DATE")))
                .rate(rs.getString("RATE"))
                .build();
    }

}
