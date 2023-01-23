package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> allGenres() {
        return genreDbStorage.allGenres();
    }

    public Genre findGenreById(int id) throws GenreNotFoundException {
        return genreDbStorage.findGenreById(id);
    }

    public List<Genre> findGenresByFilmId(int id) {
        return genreDbStorage.findGenresByFilmId(id);
    }
}
