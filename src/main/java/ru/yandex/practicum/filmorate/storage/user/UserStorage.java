package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    User newUser(@Valid @RequestBody User user);

    User changeUser(@Valid @RequestBody User user);

}
