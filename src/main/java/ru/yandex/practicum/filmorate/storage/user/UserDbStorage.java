package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;

import java.util.Collection;

@Data
@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    static final String sqlQueryCreateUser = "INSERT INTO FILMORATE_SHEMA.USERS (NAME_USER, BIRTHDAY, EMAIL, LOGIN) VALUES (?,CAST (? AS DATE),?,?)";


    private UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        log.info("Запрос mapToUser ResultSet > {}", rs);
        return User.builder()
                .id(rs.getInt("ID_USER"))
                .name(rs.getString("NAME_USER"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .build();
    }

    @Override
    public boolean getIdExist(int idUser) {
        String sqlQuery = "SELECT ID_USER FROM FILMORATE_SHEMA.USERS WHERE ID_USER = ?";
        log.info("Запрос getIdExist User > {}", sqlQuery);
        SqlRowSet idRows = jdbcTemplate.queryForRowSet(sqlQuery, idUser);
        return idRows.next();
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("Запрос create > {} --> {}", user, sqlQueryCreateUser);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryCreateUser, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getName());
            stmt.setDate(2, Date.valueOf(user.getBirthday()));
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getLogin());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE FILMORATE_SHEMA.USERS SET NAME_USER = ?, BIRTHDAY = CAST (? AS DATE), " +
                "EMAIL = ?, LOGIN = ? ";
        log.info("Запрос update > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, user.getName(), user.getBirthday(), user.getEmail(), user.getLogin());
        return user;
    }

    @Override
    public User getByIdUser(int idUser) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.USERS WHERE ID_USER = ?";
        log.info("Запрос getByIdUser > {}", sqlQuery);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapToUser, idUser);
    }

    @Override
    public void deleteByIdUser(int idUser) {
        String sqlQuery = "DELETE FROM FILMORATE_SHEMA.USERS WHERE ID_USER = ?";
        log.info("Запрос deleteByIdUser > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idUser);
    }

    @Override
    public Collection<User> getAllUser() {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.USERS AS U";
        log.info("Запрос Collection > {}", sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::mapToUser);
    }

}
