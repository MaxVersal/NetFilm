package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List< User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
       return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) throws UserNotFoundException, ValidationException {
        return userService.addFriend(id,friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend (@PathVariable("id") int id, @PathVariable("friendId") int friendId) throws UserNotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> allFriends(@PathVariable("id") int id) throws UserNotFoundException {
        return userService.getFriends(id);
    }

    @GetMapping ("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) throws UserNotFoundException {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) throws UserNotFoundException, ValidationException {
        return userService.getUserById(id);
    }

}
