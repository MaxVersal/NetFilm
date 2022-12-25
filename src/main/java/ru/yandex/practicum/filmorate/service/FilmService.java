package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        User user = userStorage.getUserById(userId);
        filmStorage.findFilmById(filmId)
                .getLikes()
                .add(user.getId());
        return filmStorage.findFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        User user = userStorage.getUserById(userId);
        filmStorage.findFilmById(filmId)
                .getLikes()
                .remove(user.getId());
        return filmStorage.findFilmById(filmId);
    }

    public Set<Integer> getLikes(int filmId) throws FilmNotFoundException {
        return filmStorage.findFilmById(filmId).getLikes();
    }

    public List<Film> popularFilms(Integer count) {
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException, FilmNotFoundException {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film searchFilmById(int id) throws FilmNotFoundException {
        return filmStorage.findFilmById(id);
    }
}
