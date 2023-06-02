package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Data
@Component
@Slf4j
@Primary
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findGenreByIdFilm(int idFilm) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.GENRE AS G JOIN FILMORATE_SHEMA.GENRE_SET AS GS ON G.ID=GS.ID WHERE GS.ID_FILM = ?";
        List<Genre> result = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToGenre(rs), idFilm).stream().toList();
        log.info("Запрос findGenreByIdFilm > {} -- {} ", sqlQuery, result);
        return result;
    }

    protected Genre mapToGenre(ResultSet rs) throws SQLException {
        log.info("Запрос mapToGenre ResultSet > {}", rs);
        return Genre.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME"))
                .build();
    }

    @Override
    public Collection<Genre> getGenres() {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.GENRE";
        log.info("Запрос коллекции getGenres > {}", sqlQuery);
        Collection<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToGenre(rs));
        log.info("Collection > {}", genres);
        return genres;
    }

    @Override
    public Genre checkGenre(int idGenre) {
        Genre genre;
        try {
            String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.GENRE WHERE ID = ?";
            genre = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> mapToGenre(rs), idGenre);
            log.info("Запрос checkGenre > {} -- {} ", sqlQuery, genre);
        } catch (Exception e) {
            log.info("Жанра с id {} нет", idGenre);
            throw new MethodArgumentNotException("Жанра с id " + idGenre + " нет.");
        }
        return genre;
    }

}
