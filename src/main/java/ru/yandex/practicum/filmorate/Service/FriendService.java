package ru.yandex.practicum.filmorate.Service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@Service
public class FriendService {

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private FriendStorage friendStorage;

    @Autowired
    private FriendService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getRequestsInFriends(int idUser) {
        return friendStorage.getFriendsRequests(idUser);
    }

}
