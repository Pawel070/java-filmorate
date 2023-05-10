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

    @Override
    public List<User> getFriendsUser(int idUser) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.USERS AS U " +
                "LEFT JOIN FILMORATE_SHEMA.FRIENDS AS F " +
                "ON (U.ID_USER = F.ID_USER) WHERE F.ID_TYPE = 1 AND F.ID_USER = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userDbStorage.mapToUser(rs, rowNum), idUser);
    }

    @Override
    public List<User> getFriendsRequests(int idUser) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.USERS AS U " +
                "LEFT JOIN FILMORATE_SHEMA.FRIENDS AS F " +
                "ON (U.ID_USER = F.ID_USER) WHERE F.ID_TYPE = 3 AND F.ID_USER = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userDbStorage.mapToUser(rs, rowNum), idUser);
    }

    @Override
    public void updateFriends(int idUser, int idFriend) {
        jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?,?,1)", idUser, idFriend);
    }

    @Override
    public void updateNoFriends(int idUser, int idFriend) {
        jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?,?,2)", idUser, idFriend);
    }

    @Override
    public void deleteFriend(int idUser, int idFriend) {
        jdbcTemplate.update("DELETE FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", idUser, idFriend);
    }

    @Override
    public void setRequestsFriends(int idUser, int idFriend) {
        jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?,?,3)", idFriend, idUser);
    }

    @Override
    public List<User> getFriendsToFriends(int firstId, int secondId) {
        String sqlQuery = "SELECT * FROM FILMORATE_SHEMA.USERS AS U LEFT JOIN FILMORATE_SHEMA.FRIENDS AS F ON (U.ID_USER = F.ID_USER) " +
                "WHERE F.ID_TYPE = 1 AND(F.ID_USER, F.ID_FRIEND) IN((F.ID_FRIEND, F.ID_USER), (F.ID_USER, F.ID_FRIEND) )";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userDbStorage.mapToUser(rs, rowNum), firstId);
    }

    @Override
    public List<User> getCommonFriends(int firstId, int secondId) {
        String sql = "SELECT * FROM USERS " +
                "WHERE ID IN (SELECT FRIEND_ID FROM FRIENDS " +
                "WHERE USER_ID = ? OR USER_ID = ? GROUP BY FRIEND_ID HAVING COUNT(*) = 2);";
        return jdbcTemplate.query(sql, (rs, rowNum) -> userDbStorage.mapToUser(rs, rowNum), firstId, secondId);
    }

}
