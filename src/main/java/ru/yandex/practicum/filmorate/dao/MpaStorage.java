package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getAllRatings();

    public Mpa getRatingById(int id) throws MpaNotFoundException;
}
