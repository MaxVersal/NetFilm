package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LikesDaoStorage implements LikesDao {

    private final JdbcTemplate jdbcTemplate;

    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;

    private final MpaService mpaService;

    public LikesDaoStorage(JdbcTemplate jdbcTemplate,
                           @Qualifier("UserDbStorage") UserStorage userStorage,
                           @Qualifier("FilmDbStorage") FilmStorage filmStorage,
                           MpaService mpaService){
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.mpaService = mpaService;
    }

    @Override
    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException {
        jdbcTemplate.update("insert into film_likes(film_id, user_id) values (?, ?)", filmId, userId);
        return filmStorage.findFilmById(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException, ValidationException {
        userStorage.getUserById(userId);
        jdbcTemplate.update("delete from FILM_LIKES where USER_ID = ? and film_id = ?", userId, filmId);
        return filmStorage.findFilmById(filmId);
    }

    @Override
    public Set<Integer> getLikes(Integer filmId) throws FilmNotFoundException {
        List<Integer> likes = jdbcTemplate.queryForList("select user_id from film_likes where film_id = ?", new Object[]{filmId}, Integer.class);
        return new HashSet<>(likes);
    }

    @Override
    public List<Film> popularFilms(Integer count) throws ValidationException {
        List<Film> pop =  jdbcTemplate.query("SELECT * FROM FILM WHERE id IN (SELECT film_id FROM FILM_LIKES GROUP BY film_id ORDER BY count(user_id)) LIMIT (?)",
                new Object[]{count}, new BeanPropertyRowMapper<>(Film.class));
        if (pop.isEmpty()) {
            return filmStorage.getFilms();
        } else {
            for (Film film : pop) {
                film.setMpa(mpaService.getRatingById(film.getMpa().getId()));
            }
            return pop;
        }
    }
}
