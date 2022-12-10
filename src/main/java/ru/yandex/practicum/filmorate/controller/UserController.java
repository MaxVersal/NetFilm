package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static final Map<Integer, User> users = new HashMap<>();
    private static int newId = 1;

    @GetMapping
    public List< User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (checkUser(user)) {
            user.setId(newId++);
            log.debug("Добавлен пользователь: {}", user);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Проверьте введенные данные!");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (checkUser(user)){
            if (users.containsKey(user.getId())) {
                log.debug("Обновлен фильм: {}", user);
                users.put(user.getId(), user);
            } else {
                throw new ValidationException("Проверьте правильность введенных данных");
            }
        } else {
            throw new ValidationException("Проверьте введенные данные!");
        }
        return user;
    }

    private boolean checkUser(User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Неверное указан email пользователя");
            return false;
        } else if (user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
            log.debug("Неверный логин");
            return false;
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Неверно указана дата рождения");
            return false;
        }

        return true;
    }
}
