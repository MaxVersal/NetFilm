package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
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

    private final JdbcTemplate jdbcTemplate;


    public UserService(UserDbStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public String addFriend(int mainUserId, int addedUserId) throws UserNotFoundException, ValidationException {
        User user = userStorage.getUserById(mainUserId);
        User friend = userStorage.getUserById(addedUserId);
        if (user == null || friend == null ) {
            throw new UserNotFoundException("Пользователь с указанным id не найден");
        }
        jdbcTemplate.update("insert into friends(user_id, friend_id) values (?,?)", mainUserId, addedUserId);
        return "Пользователь добавлен";
    }

    public String deleteFriend(int mainUserID, int deletedUserId) throws UserNotFoundException {
        String sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, mainUserID, deletedUserId);
        return "Пользователь удален из друзей";
    }

    public List<User> getFriends(int mainUserId) throws UserNotFoundException {
        return jdbcTemplate.query("select * from users where id in (select friend_id from friends where user_id = ?)",
                new Object[] {mainUserId},
                new BeanPropertyRowMapper<User>(User.class));
    }

    public List<User> getCommonFriends(int mainId, int otherId) throws UserNotFoundException {
        return jdbcTemplate.query("select * from users where id in (select friend_id from friends where user_id = ?) " +
                        "intersect " +
                        "select * from users where id in (select friend_id from friends where user_id = ?)",
                new Object[]{mainId, otherId},
                new BeanPropertyRowMapper<User>(User.class));
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

    public User getUserById(int id) throws UserNotFoundException, ValidationException {
        return userStorage.getUserById(id);
    }
}
