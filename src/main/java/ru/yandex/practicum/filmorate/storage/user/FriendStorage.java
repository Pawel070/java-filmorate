package ru.yandex.practicum.filmorate.storage.user;

import java.util.List;

import ru.yandex.practicum.filmorate.model.User;


public interface FriendStorage {

    List<User> getFriendsUser(int idUser); // друзья

    List<User> getFriendsRequests(int idUser);  // полученные заявки на друзей

    void updateFriends(int idUser, int idFriend);   // статус друг

    void updateNoFriends(int idUser, int idFriend); // недруг для блокировки

    void setRequestsFriends(int idUser, int idFriend); // заявка на дружбу

    void deleteFriend(int idUser, int idFriend);

    List<User> getFriendsToFriends(int firstId, int secondId); // взаимные друзья

    List<User> getCommonFriends(int firstId, int secondId); // друзья друзей
}
