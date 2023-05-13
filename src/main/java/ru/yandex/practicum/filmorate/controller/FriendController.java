package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;


@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class FriendController {

    private final UserService userService;

    @Autowired
    private FriendStorage friendStorage;

    @Autowired
    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriendsById(@PathVariable int id) {
        log.info("Контроллер GET User All friends> {}", id);
        return userService.findFriendsById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findFriends2UserById(@PathVariable int id, @PathVariable int otherId) {
        log.info("Контроллер GET User friends to User> {} , {}", id, otherId);
        return friendStorage.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendsUserById(@PathVariable int id, @PathVariable int friendId) {
        log.info("Контроллер PUT User add friends> {} , {}", id, friendId);
        userService.addFriendsUserById(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendsUserById(@PathVariable int id, @PathVariable int friendId) {
        log.info("Контроллер DELETE User delete friends> {} , {}", id, friendId);
        userService.deleteFriendsUserById(id, friendId);
    }

}
