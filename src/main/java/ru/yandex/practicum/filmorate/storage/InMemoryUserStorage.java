package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage  implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int newId = 1;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) throws ValidationException {
        validate(user);
        user.setId(newId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException, UserNotFoundException {
        if (users.containsKey(user.getId())) {
           validate(user);
           log.debug("Обновлен пользователь: {}", user);
           users.put(user.getId(), user);
        } else {
            throw new UserNotFoundException("Пользователя с указанным ID не существует");
        }
        return user;
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null)
            throw new UserNotFoundException("Пользователя с указанным ID не существует");

        return user;
    }

    private void validate(User user) throws ValidationException {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Неправильный email пользователя");
        } else if (user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
            throw new ValidationException("Неправильное имя пользователя");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неправильно указана дата рождения");
        }
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }
}
