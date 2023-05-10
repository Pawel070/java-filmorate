package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController implements ControllerInterface<User> {

    private UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping
    public Collection<User> selectGetting() {
        log.info("Контроллер GET все Users");
        return userService.returnAllUsers();
    }

    @Override
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Контроллер POST новый User");
        return userStorage.create(user);
    }

    @Override
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Контроллер PUT изменение User");
        return userStorage.update(user);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("Удаление пользователя с id {}", id);
        userStorage.deleteByIdUser(id);
    }

    @Override
    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        log.info("Контроллер GET User по Id> {}", id);
        return userService.findUserById(id);
    }

}
