package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int mainUserId, int addedUserId) throws UserNotFoundException {
        userStorage
                .getUserById(mainUserId)
                .getFriends()
                .add(userStorage.getUserById(addedUserId).getId());
        userStorage
                .getUserById(addedUserId)
                .getFriends()
                .add(userStorage.getUserById(mainUserId).getId());
        return userStorage.getUserById(addedUserId);
    }

    public User deleteFriend(int mainUserID, int deletedUserId) throws UserNotFoundException {
        userStorage.getUserById(mainUserID)
                .getFriends()
                .remove(deletedUserId);
        userStorage.getUserById(deletedUserId)
                .getFriends()
                .remove(mainUserID);
        return userStorage.getUserById(deletedUserId);
    }

    public Set<User> getFriends(int mainUserId) throws UserNotFoundException {
        Set<Integer> friends = userStorage.getUserById(mainUserId).getFriends();
        Set<User> friendsViewable = new HashSet<>();
        for (Integer integer : friends) {
            friendsViewable.add(userStorage.getUserById(integer));
        }
        return friendsViewable.stream()
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<User> findGeneralFriends(int mainId, int otherId) throws UserNotFoundException {
        Set<Integer> common = new HashSet<>(userStorage.getUserById(mainId).getFriends());
        common.retainAll(userStorage.getUserById(otherId).getFriends());
        Set<User> friendsViewable = new HashSet<>();
        for (Integer integer : common) {
            friendsViewable.add(userStorage.getUserById(integer));
        }
        return friendsViewable;
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

    public User searchUserById(int id) throws UserNotFoundException {
        return userStorage.getUserById(id);
    }
}
