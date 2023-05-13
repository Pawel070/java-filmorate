package ru.yandex.practicum.filmorate.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ErrorsIO.IncorrectParameterException;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private FilmStorage FilmStorage;

    @Autowired
    private FriendStorage friendStorage;

    @Autowired
    private UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> returnAllUsers() {
        log.info("returnAllUser Ok.");
        return userStorage.getAllUser();
    }

    public User findUserById(int userId) {
        User user;
        if (userId <= 0) {
            log.info("Запрос пользователя по Id - отрицательное или 0 значение Id: {}", userId);
            throw new IncorrectParameterException("Запрос пользователя по Id - неправильный формат Id.");
        }
        user = userStorage.getByIdUser(userId);
        if (user == null) {
            log.info("Запрос пользователя по Id - пользавателя нет. {}", userId);
            throw new MethodArgumentNotException("Запрос пользователя по Id пользователя с таким Id нет.");
        }
        log.info("Запрос пользователя по Id Ok. {}", user);
        return user;
    }

    public List<User> findFriendsById(int userId) {
        List<User> friendsUser;
        if (userStorage.getByIdUser(userId) == null) {
            log.info("Запрос друзей по Id пользавателя невыполним. {}", userId);
            throw new MethodArgumentNotException("Запрос пользователя по Id пользователя с таким Id нет.");
        }
        friendsUser = friendStorage.getFriendsUser(userId);
        if (friendsUser == null || friendsUser.isEmpty() || friendsUser.size() < 1) {
            log.info("Запрос друзей по Id - друзей нет. {}", userId);
            throw new ValidationException("Запрос друзей по Id пользавателя - друзей нет.");
        }
        return friendsUser;
    }

    public List<User> findFriends2UserById(int userId, int otherId) {
        if (userStorage.getByIdUser(userId) == null || userStorage.getByIdUser(otherId) == null) {
            log.info("Запрос парных друзей по Id пользавателей невыполним.");
            throw new MethodArgumentNotException("Запрос парных друзей по указанным Id пользавателей невыполним.");
        }
        log.info("Запрос парных друзей по указанным Id пользавателей создан");
        return friendStorage.getFriendsToFriends(userId, otherId);
    }

    public void addFriendsUserById(int userId, int friendId) {
        if (userStorage.getByIdUser(userId) == null || userStorage.getByIdUser(friendId) == null) {
            log.info("Запрос добавления в друзья по Id пользавателя невыполним.");
            throw new MethodArgumentNotException("Запрос добавления в друзья по Id пользавателя невыполним - пользователя с таким Id нет.");
        } else {
            friendStorage.setRequestsFriends(userId, friendId);
            log.info("Запрос в друзья отправлен.");
        }
    }

    public void deleteFriendsUserById(int userId, int friendId) {
        if (userStorage.getByIdUser(userId) == null || userStorage.getByIdUser(friendId) == null) {
            log.info("Запрос удаления из друзей по Id пользавателя невыполним.");
            throw new MethodArgumentNotException("Запрос удаления из друзей по Id пользавателя невыполним - пользователя с таким Id нет.");
        } else {
            friendStorage.deleteFriend(userId, friendId);
            log.info("Удаление из друзей выполнено.");
        }
    }

}