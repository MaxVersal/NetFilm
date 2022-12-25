package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    User getUserById(int id) throws ValidationException;

}
