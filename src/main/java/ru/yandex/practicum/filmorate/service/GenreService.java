package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    private final JdbcTemplate jdbcTemplate;

    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> allGenres() {
        return jdbcTemplate.query("select * from genre", new BeanPropertyRowMapper<>(Genre.class));
    }

    public Genre findGenreById(int id) throws GenreNotFoundException {
        SqlRowSet genreSet = jdbcTemplate.queryForRowSet("select * from genre where id = ?", id);
        if (genreSet.next()) {
            Genre genre = new Genre(genreSet.getInt("id"), genreSet.getString("name"));
            return genre;
        } else {
            throw new GenreNotFoundException("Не найден жанр с указанным айди");
        }
    }

    public List<Genre> findGenresByFilmId(int id) {
        return jdbcTemplate.query("select id from genre where id in (select genre_id from film_genre where film_id = ?)",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Genre.class));
    }
}
