package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ValidationService {

    public void validateFilm (Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Имя фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть длиннее 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 1, 28))) {
            throw new ValidationException("Некорректная дата фильма");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Неккоректная длительность фильма");
        }
    }

    public void validateUser (User user) throws ValidationException {
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
