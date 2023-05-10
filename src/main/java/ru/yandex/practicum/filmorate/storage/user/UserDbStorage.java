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
    static final String sqlQueryCreateUser = "INSERT INTO USERS (NAME, BIRTHDAY, EMAIL, LOGIN) VALUES (?,?,?,?)";


    private UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .idUser(rs.getInt("ID_USER"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .build();
    }

    @Override
    public boolean getIdExist(int idUser) {
        SqlRowSet idRows = jdbcTemplate.queryForRowSet("SELECT ID_USERS FROM USERS WHERE ID_USERS = ?", idUser);
        return idRows.next();
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("Запрос > {}", sqlQueryCreateUser);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryCreateUser, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(2, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getLogin());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        user.setIdUser(id);
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE USERS SET ID_USER = ?, NAME = ?, BIRTHDAY = ?, " +
                "EMAIL = ?, LOGIN = ? ";
        log.info("Запрос > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, user.getIdUser(), user.getName(), user.getBirthday(),
                user.getEmail(), user.getLogin());
        return user;
    }

    @Override
    public User getByIdUser(int idUser) {
        String sqlQuery = "SELECT * FROM USERS WHERE ID_USER = ?";
        log.info("Запрос > {}", sqlQuery);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapToUser, idUser);
    }

    @Override
    public void deleteByIdUser(int idUser) {
        String sqlQuery = "DELETE FROM USERS WHERE ID_USER = ?";
        log.info("Запрос > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idUser);
    }

    @Override
    public Collection<User> getAllUser() {
        String sqlQuery = "SELECT * FROM USERS AS U";
        log.info("Запрос > {}", sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::mapToUser);
    }

}
