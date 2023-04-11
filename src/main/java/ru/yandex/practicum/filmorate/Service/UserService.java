package ru.yandex.practicum.filmorate.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ErrorsIO.IncorrectParameterException;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    private UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> returnAllUsers() {
        log.info("returnAllUser Ok. {}", inMemoryUserStorage.getUsers().values());
        return inMemoryUserStorage.getUsers().values();
    }

    public User findUserById(String userId) {
        User userReturn;
        if (userId == null) {
            log.info("Запрос пользователя по Id - неправильный формат Id: {}", userId);
            userReturn = null;
            throw new IncorrectParameterException("Запрос пользователя по Id - неправильный формат Id.");
        } else {
            int id = Integer.parseInt(userId);
            if (id <= 0) {
                log.info("Запрос пользователя по Id - отрицательное или 0 значение Id: {}", userId);
                userReturn = null;
                throw new IncorrectParameterException("Запрос пользователя по Id - неправильный формат Id.");
            } else {
                userReturn = inMemoryUserStorage.getUsers().get(id);
                if (userReturn == null) {
                    log.info("Запрос пользователя по Id - пользавателя нет. {}", userId);
                    throw new MethodArgumentNotException("Запрос пользователя по Id пользователя с таким Id нет.");
                }
            }
        }
        log.info("Запрос пользователя по Id Ok. {}", userReturn);
        return userReturn;
    }

    public Collection<User> findFriendsById(String userId) {
        User userReturn = findUserById(userId);
        Set<Long> friends;
        Collection<User> friendsUser = new ArrayList<>();
        if (userReturn == null) {
            log.info("Запрос друзей по Id пользавателя невыполним. {}", userReturn);
            throw new MethodArgumentNotException("Запрос пользователя по Id пользователя с таким Id нет.");
        } else {
            friends = userReturn.getFriends();
            if (friends == null || friends.isEmpty() || friends.size() < 1) {
                log.info("Запрос друзей по Id - друзей нет. {}", userId);
                throw new ValidationException("Запрос друзей по Id пользавателя - друзей нет.");
            } else {
                for (long item : friends) {
                    friendsUser.add(findUserById(Long.toString(item)));
                }
                log.info("Запрос друзей по Id пользавателя Ok. {}", friendsUser);
            }
        }
        return friendsUser;
    }

    public Collection<User> findFriends2UserById(String userId, String otherId) {
        Collection<User> friendsUser1 = findFriendsById(userId);
        Collection<User> friendsUser2 = findFriendsById(otherId);
        if (friendsUser1 == null || friendsUser2 == null || friendsUser1.isEmpty() || friendsUser2.isEmpty()) {
            log.info("Запрос парных друзей по Id пользавателей невыполним.");
            friendsUser1 = null;
            throw new MethodArgumentNotException("Запрос парных друзей по указанным Id пользавателей невыполним.");
        } else {
            friendsUser1.retainAll(friendsUser2);
            friendsUser2.retainAll(friendsUser1);
            log.info("Запрос парных друзей по указанным Id пользавателей Ok. {}", friendsUser1);
        }
        return friendsUser1;
    }

    public User addFriendsUserById(String userId, String friendId) {
        User userReturn1 = findUserById(userId);
        User userReturn2 = findUserById(friendId);
        Set<Long> friends = userReturn1.getFriends();
        if (userReturn1 == null || userReturn2 == null) {
            log.info("Запрос добавления в друзья по Id пользавателя невыполним. {} - {}", userReturn1, userReturn2);
            throw new MethodArgumentNotException("Запрос добавления в друзья по Id пользавателя невыполним - пользователя с таким Id нет.");
        } else {
            friends.add(Long.parseLong(friendId)); // 1 друг 2
            userReturn1.setFriends(friends);
            inMemoryUserStorage.changeUser(userReturn1);
            friends = userReturn2.getFriends();    // 2 друг 1
            friends.add(Long.parseLong(userId));
            userReturn2.setFriends(friends);
            inMemoryUserStorage.changeUser(userReturn2);
            log.info("Запрос парных друзей по указанным Id пользавателей Ok. {} - {}", userReturn1, userReturn2);
        }
        return userReturn1;
    }

    public User deleteFriendsUserById(String userId, String friendId) {
        User userReturn1 = findUserById(userId);
        User userReturn2 = findUserById(friendId);
        Set<Long> friends = userReturn1.getFriends();
        if (userReturn1 == null || userReturn2 == null) {
            log.info("Запрос удаления из друзей по Id пользавателя невыполним. {} - {}", userReturn1, userReturn2);
            throw new MethodArgumentNotException("Запрос удаления из друзей по Id пользавателя невыполним - пользователя с таким Id нет.");
        } else {
            friends.remove(Long.parseLong(friendId)); // 1 друг 2
            userReturn1.setFriends(friends);
            inMemoryUserStorage.changeUser(userReturn1);
            friends = userReturn2.getFriends();    // 2 друг 1
            friends.remove(Long.parseLong(userId));
            userReturn2.setFriends(friends);
            inMemoryUserStorage.changeUser(userReturn2);
            log.info("Запрос парного удаления друзей по указанным Id пользавателей Ok. {} - {}", userReturn1, userReturn2);
        }
        return userReturn1;
    }

    public User findUserByEmail(String email) {
        if (email == null) {
            log.info("Неправильный формат Email: {}", email);
            return null;
        }
        return inMemoryUserStorage.getUsers().get(email);
    }

 }
