package ru.yandex.practicum.filmorate.storage.user;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.User;


public interface UserStorage {

    User create(User user);

    User update(User user);

    void deleteByIdUser(int idUser);

    Collection<User> getAllUser();

    User getByIdUser(int idUser);

    boolean getIdExist(int idUser);

}
