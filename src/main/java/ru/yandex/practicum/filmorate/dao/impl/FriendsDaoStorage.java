package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Component
public class FriendsDaoStorage implements FriendsDao {
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    private final JdbcTemplate jdbcTemplate;

    public FriendsDaoStorage(@Qualifier("UserDbStorage")UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String addFriend(int mainUserId, int addedUserId) throws UserNotFoundException, ValidationException {
        User user = userStorage.getUserById(mainUserId);
        User friend = userStorage.getUserById(addedUserId);
        if (user == null || friend == null ) {
            throw new UserNotFoundException("Пользователь с указанным id не найден");
        }
        jdbcTemplate.update("insert into friends(user_id, friend_id) values (?,?)", mainUserId, addedUserId);
        return "Пользователь добавлен";
    }

    @Override
    public String deleteFriends(int mainUserId, int deletedUserId) throws UserNotFoundException {
        String sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, mainUserId, deletedUserId);
        return "Пользователь удален из друзей";
    }

    @Override
    public List<User> getFriends(int mainUserId) throws UserNotFoundException {
        return jdbcTemplate.query("select * from users where id in (select friend_id from friends where user_id = ?)",
                new Object[] {mainUserId},
                new BeanPropertyRowMapper<User>(User.class));
    }

    @Override
    public List<User> getCommonFriends(int mainId, int otherId) throws UserNotFoundException {
        return jdbcTemplate.query("select * from users where id in (select friend_id from friends where user_id = ?) " +
                        "intersect " +
                        "select * from users where id in (select friend_id from friends where user_id = ?)",
                new Object[]{mainId, otherId},
                new BeanPropertyRowMapper<User>(User.class));
    }
}
