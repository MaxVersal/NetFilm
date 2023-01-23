package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface LikesDao {

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException;

    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException;

    public Set<Integer> getLikes(Integer filmId) throws FilmNotFoundException;

    public List<Film> popularFilms(Integer count) throws ValidationException;

}
