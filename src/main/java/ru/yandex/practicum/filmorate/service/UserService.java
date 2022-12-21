package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(int mainUserId, int addedUserId) throws UserNotFoundException {
        userStorage
                .getUserById(mainUserId)
                .getFriends()
                .add(userStorage.getUserById(addedUserId).getId());
        userStorage
                .getUserById(addedUserId)
                .getFriends()
                .add(userStorage.getUserById(mainUserId).getId());
        return String.format("Пользователь %s и %s теперь друзья", userStorage.getUserById(mainUserId).getName(),
                                                                    userStorage.getUserById(addedUserId).getName());

    }

    public String deleteFriend(int mainUserID, int deletedUserId) throws UserNotFoundException {
        userStorage.getUserById(mainUserID)
                .getFriends()
                .remove(deletedUserId);
        userStorage.getUserById(deletedUserId)
                .getFriends()
                .remove(mainUserID);
        return String.format("Пользователи %s и %s больше не друзья", userStorage.getUserById(mainUserID).getName(),
                                                                        userStorage.getUserById(deletedUserId).getName());
    }

    public List<User> getFriends(int mainUserId) throws UserNotFoundException {
        Set<Integer> friends = userStorage.getUserById(mainUserId).getFriends();
        return friends.stream()
                .map(userId -> userStorage.getUserById(userId))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int mainId, int otherId) throws UserNotFoundException {
        Set<Integer> common = new HashSet<>(userStorage.getUserById(mainId).getFriends());
        return common.stream()
                .filter(userStorage.getUserById(otherId).getFriends() :: contains)
                .map(userID -> userStorage.getUserById(userID))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    public User addUser(User user) throws ValidationException {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(int id) throws UserNotFoundException {
        return userStorage.getUserById(id);
    }
}
