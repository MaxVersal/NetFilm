package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    public List<Genre> allGenres();

    public Genre findGenreById(int id) throws GenreNotFoundException;

    public List<Genre> findGenresByFilmId(int id);
}
