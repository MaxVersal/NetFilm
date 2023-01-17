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

    private final MpaService mpaService;

    private final GenreService genreService;

    @Autowired
    public FilmService(FilmDbStorage filmStorage,
                       UserDbStorage userStorage,
                       JdbcTemplate jdbcTemplate,
                       MpaService mpaService,
                       GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException {
        jdbcTemplate.update("insert into film_likes(film_id, user_id) values (?, ?)", filmId, userId);
        return filmStorage.findFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException {
        userStorage.getUserById(userId);
        jdbcTemplate.update("delete from FILM_LIKES where USER_ID = ? and film_id = ?", userId, filmId);
        return filmStorage.findFilmById(filmId);
    }

    public Set<Integer> getLikes(Integer filmId) throws FilmNotFoundException {
        List<Integer> likes = jdbcTemplate.queryForList("select user_id from film_likes where film_id = ?", new Object[]{filmId}, Integer.class);
        return new HashSet<>(likes);
    }

    public List<Film> popularFilms(Integer count) throws ValidationException {
        List<Film> pop =  jdbcTemplate.query("SELECT * FROM FILM WHERE id IN (SELECT film_id FROM FILM_LIKES GROUP BY film_id ORDER BY count(user_id)) LIMIT (?)",
                new Object[]{count}, new BeanPropertyRowMapper<>(Film.class));
        if (pop.isEmpty()) {
            return getAllFilms();
        } else {
            for (Film film : pop) {
                film.setMpa(mpaService.getRatingById(film.getMpa().getId()));
            }
            return pop;
        }

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
