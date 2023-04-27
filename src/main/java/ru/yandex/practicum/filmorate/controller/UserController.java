package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private UserStorage inMemoryUserStorage;
    private UserService userService;
    private FilmService filmService;

    @Autowired
    public void UserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public Collection<User> returnAllUsers() {
        log.info("Контроллер GET все Users> {}", userService.returnAllUsers());
        return userService.returnAllUsers();
    }

    @PostMapping
    public User newUser(@Valid @RequestBody User user) {
        log.info("Контроллер POST новый User> {}", user);
        return inMemoryUserStorage.newUser(user);
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Контроллер PUT изменение User> {}", user);
        return inMemoryUserStorage.changeUser(user);
    }

    @GetMapping("/user/{userMail}")
    public User getUserMail(@PathVariable("userMail") String userMail) {
        log.info("Контроллер GET User по Email> {}", userMail);
        return userService.findUserByEmail(userMail);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable String id) {
        log.info("Контроллер GET User по Id> {}", id);
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriendsById(@PathVariable String id) {
        log.info("Контроллер GET User friends> {}", id);
        return userService.findFriendsById(id);
    }
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findFriends2UserById(@PathVariable String id, @PathVariable String otherId) {
        log.info("Контроллер GET User friends to User> {} , {}", id, otherId);
        return userService.findFriends2UserById(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriendsUserById(@PathVariable String id, @PathVariable String friendId) {
        log.info("Контроллер PUT User add friends> {} , {}", id, friendId);
        return userService.addFriendsUserById(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriendsUserById(@PathVariable String id, @PathVariable String friendId) {
        log.info("Контроллер DELETE User delete friends> {} , {}", id, friendId);
        return userService.deleteFriendsUserById(id, friendId);
    }

}
