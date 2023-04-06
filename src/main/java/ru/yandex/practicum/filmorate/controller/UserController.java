package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Data;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    protected Map<Integer, User> users = new HashMap<>();
    private static int numberId = 0;

    private int servisId() {
        numberId++;
        return numberId;
    }

    @GetMapping
    public Collection<User> returnAllUsers() {
        return users.values();
    }

    @PostMapping
    public User newUser(@Valid @RequestBody User user)  {
        User retUser;
        user.setId(servisId());
        if (users.containsKey(user.getId())) {
            throw new ValidationException("E09 Пользователь с таким ID уже внесён. Смените ID.");
        }
        users.put(user.getId(), user);
        retUser = user;
        return retUser;
    }
    @PutMapping
    public User changeUser(@Valid @RequestBody User user)  {
        User retUser;
        if ((users.containsKey(user.getId()))) {
            throw new ValidationException("E05 Пользователь с таким ID не существует. Смените ID.");
        }
        User userBufer = users.get(user.getId());
        users.remove(user.getId());
        if (newUser(user) == null) {
            users.put(user.getId(), userBufer);
            throw new ValidationException("E06 Изменения не внесены.");
        }
        retUser = user;
        return retUser;
    }

}

