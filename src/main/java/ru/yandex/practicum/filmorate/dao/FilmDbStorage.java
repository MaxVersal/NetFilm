package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.print.DocFlavor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final ValidationService validate = new ValidationService();

    private final GenreService genreService;

    private final MpaService mpaService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreService genreService, MpaService mpaService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    @Override
    public List<Film> getFilms() throws ValidationException {
        String sql = "select * from film";
        List<Film> films = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Film.class));
        for (Film film : films) {
            Set<Integer> genres = new HashSet<>(genreService.findGenresByFilmId(film.getId()));
            Set<Genre> genresForCurrentFilm = new TreeSet<>(Comparator.comparingInt(Genre::getId));
            for (Integer genreId : genres) {
                genresForCurrentFilm.add(genreService.findGenreById(genreId));
            }
            film.setGenres(genresForCurrentFilm);
            Mpa mpa = new Mpa(film.getMpa().getId(),jdbcTemplate.queryForObject("select name from mpa where rating_id in (select mpa from film where id = ?)", new Object[]{film.getId()}, String.class));
            film.setMpa(mpa);
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        validate.validateFilm(film);
        film.getMpa().setName(jdbcTemplate.queryForObject("select name from mpa where rating_id = ?",
                new Object[]{film.getMpa().getId()}, String.class));
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        Set<Genre> genresForFilm = film.getGenres();
        String sqlquery = "insert into  film(NAME,description,release_date,duration, mpa) " +
                "values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(
          connection -> {
              PreparedStatement ps = connection.prepareStatement(sqlquery, new String[]{"id"});
              ps.setString(1, film.getName());
              ps.setString(2,film.getDescription());
              ps.setDate(3, Date.valueOf(film.getReleaseDate()));
              ps.setInt(4, film.getDuration());
              ps.setInt(5, film.getMpa().getId());
              return ps;
          }, keyHolder
        );
        film.setId(keyHolder.getKey().intValue());
        if (genresForFilm != null) {
            for (Genre genre : genresForFilm) {
                String name = jdbcTemplate.queryForObject("select name from genre where id = ?",
                        new Object[]{genre.getId()},
                        String.class);
                genre.setName(name);
                genres.add(genre);
                jdbcTemplate.update("insert into film_genre(film_id, genre_id) values(?,?)", film.getId(), genre.getId());
            }
            film.setGenres(genres);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        validate.validateFilm(film);
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where id = ?", film.getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (filmRows.next()) {
            film.getMpa().setName(jdbcTemplate.queryForObject("select name from mpa where rating_id = ?",
                    new Object[]{film.getMpa().getId()}, String.class));
            Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
            Set<Genre> genresForFilm = film.getGenres();
            if (genresForFilm != null){
                for (Genre genre : genresForFilm) {
                    String name = jdbcTemplate.queryForObject("select name from genre where id = ?",
                            new Object[]{genre.getId()},
                            String.class);
                    genre.setName(name);
                    genres.add(genre);
                    jdbcTemplate.update("insert into film_genre(film_id, genre_id) values(?,?)", film.getId(), genre.getId());
                }
                film.setGenres(genres);
            }
            String sqlquery = "update film set id = ?, name = ?, description = ?, release_date = ?, duration = ?, mpa = ?" +
                    "where id = ?";
            jdbcTemplate.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(sqlquery, new String[]{"id"});
                        ps.setInt(1, film.getId());
                        ps.setString(2, film.getName());
                        ps.setString(3,film.getDescription());
                        ps.setDate(4, Date.valueOf(film.getReleaseDate()));
                        ps.setInt(5, film.getDuration());
                        ps.setInt(6, film.getMpa().getId());
                        ps.setInt(7, film.getId());
                        return ps;
                    }, keyHolder
            );
            return film;
        } else {
            throw new FilmNotFoundException("Фильма с данным id не существует");
        }
    }

    @Override
    public Film findFilmById(int id) throws ValidationException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where id = ?", id);
        String name;
        try {
            name = jdbcTemplate.queryForObject("select name from mpa where rating_id in (select mpa from film where id = ?)",
                    new Object[]{id},
                    String.class);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("Фильм с данным id не найден");
        }
        if (filmRows.next()) {
            Mpa mpa = new Mpa(filmRows.getInt("MPA"), name);
            Film film = new Film(
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    mpa
            );
            film.setId(id);

            log.info("Найден фильм с id: {}", film.getId());
            return film;
        } else {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        }
    }


}
