package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private static int numberId = 0;

    private int servisId() {
        numberId++;
        log.info("set ServisId: {}", numberId);
        return numberId;
    }

    @GetMapping
    public Collection<User> returnAllUsers() {
        log.info("returnAllFilms Ok. {}", users.values());
        return users.values();
    }

    @PostMapping
    public User newUser(@Valid @RequestBody User user) {
        User userRet;
         int buferId = user.getId();
        if (user.getName() == null) {
            log.info("ERR06  было >{}", user);
            user.setName(user.getLogin());
            log.info("ERR06 стало >{}", user);
            // throw new ValidationException("E12 Имя не должно быть пустым. Использован логин.");
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
        if (userRet == null) {
            throw new ValidationException("E09 Пользователь с таким Email уже внесён.");
        } else {
            user.setId(servisId());
            users.put(user.getId(), user);
            log.info("newUser post Ok. {}", user);
            userRet = user;
        }
        return user;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        Boolean validIO = true;
        log.info("changeUser: {}", user);
        int buferId = user.getId();
        log.info("buferId: {}", buferId);
        log.info("users.containsKey(buferId): {}", users.containsKey(buferId));
        if (users.containsKey(buferId) != true && validIO == true) {
            log.error("ERR05 - {}", users.containsKey(buferId));
            validIO = false;
            throw new ValidationException("E05 Пользователь с таким ID не существует. Смените ID.");
        }
        log.info("далее.... ");
        if (validIO == true) {
            log.info("user удаляем ... {}", users.get(buferId));
            users.remove(buferId);
            log.info("user новый заносим ... {}", user);
            users.put(user.getId(), user);
            log.info("changeUser put Ok. {}", user);
            return user;
        } else {
            log.error("changeFilm not put`s {}", users.get(buferId));
            return null;
        }
    }

}
