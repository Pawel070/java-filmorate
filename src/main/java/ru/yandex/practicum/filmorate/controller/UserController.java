package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Data;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Data
@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();
    protected Map<Integer, User> userMap = new HashMap<>();
    public static int numberId = 0;

    public int servisId() {
        numberId++;
        return numberId;
    }

    @GetMapping
    public Collection<User> findAll() {
        return returnAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        return newUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        return changeUser(user);
    }

    public Collection<User> returnAllUsers() {
        return userMap.values();
    }

    public User newUser(User user) throws ValidationException {
        User retUser;
        user.setId(servisId());
        if (userMap.containsKey(user.getId())) {
            throw new ValidationException("E09 Пользователь с таким ID уже внесён. Смените ID.");
        }
        userMap.put(user.getId(), user);
        retUser = user;
        return retUser;
    }

    public User changeUser(User user) throws ValidationException {
        User retUser;
        if ((userMap.containsKey(user.getId()))) {
            throw new ValidationException("E05 Пользователь с таким ID не существует. Смените ID.");
        }
        User userBufer = userMap.get(user.getId());
        userMap.remove(user.getId());
        if (newUser(user) == null) {
            userMap.put(user.getId(), userBufer);
            throw new ValidationException("E06 Изменения не внесены.");
        }
        retUser = user;
        return retUser;
    }

}

