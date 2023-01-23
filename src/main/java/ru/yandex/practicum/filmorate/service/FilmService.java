package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Set;

@Service
public class FilmService {

    private final LikesDao likesStorage;

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;

    private final ValidationService validationService;

    @Autowired
    public FilmService(LikesDao likesStorage,
                       @Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       ValidationService validationService){
        this.filmStorage = filmStorage;
        this.likesStorage= likesStorage;
        this.validationService = validationService;
    }

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException {
        return likesStorage.addLike(filmId, userId);
    }

    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException {
        return likesStorage.deleteLike(filmId, userId);
    }

    public Set<Integer> getLikes(Integer filmId) throws FilmNotFoundException {
        return likesStorage.getLikes(filmId);
    }

    public List<Film> popularFilms(Integer count) throws ValidationException {
        return likesStorage.popularFilms(count);
    }

    public Film addFilm(Film film) throws ValidationException {
        validationService.validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException, FilmNotFoundException {
        validationService.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() throws ValidationException {
        return filmStorage.getFilms();
    }

    public Film searchFilmById(int id) throws FilmNotFoundException, ValidationException {
        return filmStorage.findFilmById(id);
    }
}
