package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Repository
@Component
public abstract class InMemoryFriendStorage implements FriendStorage {

    private Map<Integer, Friend> friendMap = new HashMap<>();

    private int numberId;

    private int servisId() {
        numberId++;
        log.info("set ServisId: {}", numberId);
        return numberId;
    }

    @Override
    public void updateFriends(int idUser, int idFriend) {
    }

    @Override
    public void updateNoFriends(int idUser, int idFriend) {
    }

    @Override
    public void setRequestsFriends(int idUser, int idFriend) {
    }

    @Override
    public void deleteFriend(int idUser, int idFriend) {
    }

}
