package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;


    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<User> getUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User addUser(User user) throws ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "insert into users(email,login,name,birthday) values (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder
        );
        user.setId(keyHolder.getKey().intValue());
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        int rows = jdbcTemplate.update("update users set email = ?, login = ?, name = ?, birthday = ? where id = ?",  user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        if (rows != 0) {
            log.info("Обновлен пользователь с id: {}", user.getId());
            return user;
        } else {
            throw new UserNotFoundException("Пользователь с данным id не найден");
        }
    }

    @Override
    public User getUserById(int id) throws ValidationException {
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRow.next()) {
            User user = new User(
                    userRow.getInt("id"),
                    userRow.getString("email"),
                    userRow.getString("login"),
                    userRow.getString("name"),
                    userRow.getDate("birthday").toLocalDate()
            );
            log.info("Найден пользователь с id:{}", id);
            return user;
        } else {
            throw new UserNotFoundException("Пользователь с данным id не существует");
        }
    }
}
