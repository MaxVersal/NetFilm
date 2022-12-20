package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List< User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) throws ValidationException {
       return userService.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) throws UserNotFoundException {
        return userService.addFriend(id,friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend (@PathVariable("id") int id, @PathVariable("friendId") int friendId) throws UserNotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> allFriends(@PathVariable("id") int id) throws UserNotFoundException {
        return userService.getFriends(id);
    }

    @GetMapping ("/users/{id}/friends/common/{otherId}")
    public Set<User> commonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) throws UserNotFoundException {
        return userService.findGeneralFriends(id, otherId);
    }

    @GetMapping("/users/{id}")
    public User searchUser(@PathVariable("id") int id) throws UserNotFoundException {
        return userService.searchUserById(id);
    }

}
