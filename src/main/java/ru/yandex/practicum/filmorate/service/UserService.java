package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    @Qualifier ("UserDbStorage")
    private final UserStorage userStorage;

    private final FriendsDao friendsStorage;

    private final ValidationService validationService;


    public UserService(FriendsDao friendsStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       ValidationService validationService) {
        this.friendsStorage = friendsStorage;
        this.userStorage = userStorage;
        this.validationService=validationService;
    }

    public String addFriend(int mainUserId, int addedUserId) throws UserNotFoundException, ValidationException {
        return friendsStorage.addFriend(mainUserId, addedUserId);
    }

    public String deleteFriend(int mainUserID, int deletedUserId) throws UserNotFoundException {
        return friendsStorage.deleteFriends(mainUserID,deletedUserId);
    }

    public List<User> getFriends(int mainUserId) throws UserNotFoundException {
        return friendsStorage.getFriends(mainUserId);
    }

    public List<User> getCommonFriends(int mainId, int otherId) throws UserNotFoundException {
        return friendsStorage.getCommonFriends(mainId,otherId);
    }

    public User addUser(User user) throws ValidationException {
        validationService.validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        validationService.validateUser(user);
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(int id) throws UserNotFoundException, ValidationException {
        return userStorage.getUserById(id);
    }
}
