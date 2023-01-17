package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FilmService {

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage,JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException {
        jdbcTemplate.update("insert into film_likes(film_id, user_id) values (?, ?)", filmId, userId);
        return filmStorage.findFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException {
        jdbcTemplate.update("delete from FILM_LIKES where USER_ID = ? and film_id = ?", userId, filmId);
        return filmStorage.findFilmById(filmId);
    }

    public Set<Integer> getLikes(Integer filmId) throws FilmNotFoundException {
        List<Integer> likes = jdbcTemplate.queryForList("select user_id from film_likes where film_id = ?", new Object[]{filmId}, Integer.class);
        return new HashSet<>(likes);
    }

    public List<Film> popularFilms(Integer count) {
        List<Film> pop =  jdbcTemplate.query("select * from FILM where id in (select id from film_likes order by COUNT(user_id) desc limit(?))",
                new Object[]{count}, new BeanPropertyRowMapper<>(Film.class));
        return pop;
    }

    public Film addFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException, FilmNotFoundException {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() throws ValidationException {
        return filmStorage.getFilms();
    }

    public Film searchFilmById(int id) throws FilmNotFoundException, ValidationException {
        return filmStorage.findFilmById(id);
    }
}
