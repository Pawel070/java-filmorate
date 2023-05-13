package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Data
@Slf4j
@Validated
@Repository
@Component
public abstract class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();

    private int numberId;

    private int servisId() {
        numberId++;
        log.info("set ServisId: {}", numberId);
        return numberId;
    }

    @Override
    public User create(@Valid @RequestBody User user) {
        User userRet;
        int buferId = user.getIdUser();
        if (user.getName() == null) {
            log.info("ERR06  было >{}", user);
            user.setName(user.getLogin());
            log.info("ERR06 стало >{}", user);
        }
        log.info("newUser: {}", user);
        userRet = user;
        for (Integer integer : users.keySet()) {
            log.info("newUser проверяем ...: {}", users.get(integer));
            if (users.get(integer).getEmail().equals(user.getEmail())) {
                log.info("Email уже существует: {}", users.get(integer));
                userRet = null;
            }
        }
        if (userRet != null) {
            user.setIdUser(servisId());
            users.put(user.getIdUser(), user);
            log.info("newUser post Ok. {}", user);
        } else {
            log.info("Email уже существует: {}", user.getEmail());
            throw new ValidationException("E09 Пользователь с таким Email уже внесён.");
        }
        return user;
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        int buferId = user.getIdUser();
        log.info("changeUser: {}  buferId: {} users.containsKey(buferId): {}", user, buferId, users.containsKey(buferId));
        if (users.containsKey(buferId)) {
            users.remove(buferId);
            users.put(user.getIdUser(), user);
            log.info("changeUser put Ok. {}", user);
        } else {
            log.error("ERR05 - {}", users.containsKey(buferId));
            throw new MethodArgumentNotException("E05 Пользователь с таким ID не существует. Смените ID.");
        }
        return user;
    }

    @Override
    public void deleteByIdUser(int idUser) {
    }

    @Override
    public Collection<User> getAllUser() {
        return null;
    }

    @Override
    public User getByIdUser(int idUser) {
        return null;
    }

}
