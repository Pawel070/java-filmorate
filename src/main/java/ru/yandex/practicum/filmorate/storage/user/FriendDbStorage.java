package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

@Data
@Slf4j
@Component
@Primary
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    private FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public UserDbStorage userDbStorage;


    private List<User> getFriends(int idUser, int idType) {
        String sqlQuery = "SELECT U.ID_USER, U.NAME_USER, U.BIRTHDAY, U.EMAIL, U.LOGIN  FROM FILMORATE_SHEMA.USERS AS U " +
                "LEFT JOIN FILMORATE_SHEMA.FRIENDS AS F " +
                "ON (U.ID_USER = F.ID_USER) WHERE F.ID_TYPE = ? AND U.ID_USER = ?";
        log.info("Запрос getFriends > {} type {}", sqlQuery, idType);
        List<User> result = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userDbStorage.mapToUser(rs, rowNum), idType, idUser);
        log.info("Возврат getFriends > id {} type {} > {}", idUser, idType, result);
        return result;
    }

    @Override
    public List<User> getFriendsUser(int idUser) {
        return getFriends(idUser, 1);
    }

    @Override
    public List<User> getFriendsRequests(int idUser) {
        return getFriends(idUser, 3);
    }

    @Override
    public void updateFriends(int idUser, int idFriend) {
        String sqlQuery = "INSERT INTO FILMORATE_SHEMA.FRIENDS (ID_USER, ID_FRIEND, ID_TYPE) VALUES (?,?,1)";
        log.info("Запрос updateFriends > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idUser, idFriend);
    }

    @Override
    public void updateNoFriends(int idUser, int idFriend) {
        String sqlQuery = "INSERT INTO FILMORATE_SHEMA.FRIENDS (ID_USER, ID_FRIEND, ID_TYPE) VALUES (?,?,?)";
        log.info("Запрос updateNoFriends > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idUser, idFriend, '2');
    }

    @Override
    public void deleteFriend(int idUser, int idFriend) {
        String sqlQuery = "DELETE FILMORATE_SHEMA.FRIENDS WHERE ID_USER = ? AND ID_FRIEND = ?";
        log.info("Запрос deleteFriend > {}", sqlQuery);
        jdbcTemplate.update(sqlQuery, idUser, idFriend);
    }

    @Override
    public void setRequestsFriends(int idUser, int idFriend) {
        String sqlQuery = "INSERT INTO FILMORATE_SHEMA.FRIENDS (ID_USER, ID_FRIEND, ID_TYPE) VALUES (?,?,?)";
        log.info("Запрос setRequestsFriends > {} + {} == {}",idUser, idFriend, sqlQuery);
        jdbcTemplate.update(sqlQuery, idUser, idFriend, '3');
    }

    @Override
    public List<User> getFriendsToFriends(int idType) {
        String sqlQuery = "SELECT U.ID_USER, U.NAME_USER, U.BIRTHDAY, U.EMAIL, U.LOGIN  " +
                "FROM FILMORATE_SHEMA.USERS AS U LEFT JOIN FILMORATE_SHEMA.FRIENDS AS F ON (U.ID_USER = F.ID_USER) " +
                "WHERE F.ID_TYPE = ? AND(F.ID_USER, F.ID_FRIEND) IN((F.ID_FRIEND, F.ID_USER), (F.ID_USER, F.ID_FRIEND) )";
        log.info("Запрос getFriendsToFriends > {} type {}", sqlQuery, idType);
        List<User> result = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userDbStorage.mapToUser(rs, rowNum), idType);
        log.info("Возврат getFriends > type {} > {}", idType, result);
        return result;
    }

    @Override
    public List<User> getCommonFriends(int firstId, int secondId) {
        String sqlQuery = "SELECT U.ID_USER, U.NAME_USER, U.BIRTHDAY, U.EMAIL, U.LOGIN  FROM FILMORATE_SHEMA.USERS AS U\n" +
                " WHERE ID_USER IN (SELECT ID_FRIEND FROM FILMORATE_SHEMA.FRIENDS\n" +
                " WHERE ID_USER = ? OR ID_USER = ? GROUP BY ID_FRIEND HAVING COUNT(*) = 2);"; // без учета типа дружбы
        log.info("Запрос getFriendsToFriends > первый {} второй {} > {}", firstId, secondId, sqlQuery);
        List<User> result = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userDbStorage.mapToUser(rs, rowNum), firstId, secondId);
        log.info("Возврат getFriends > {}", result);
        return result;
    }

}
