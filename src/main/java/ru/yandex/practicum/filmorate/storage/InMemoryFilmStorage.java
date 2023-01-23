package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private Map<Integer, Film> films = new HashMap<>();
    private int newId = 1;

    private final ValidationService validate = new ValidationService();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        validate.validateFilm(film);
        film.setId(newId++);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            validate.validateFilm(film);
            films.put(film.getId(), film);
        } else {
            throw new FilmNotFoundException("Не найден указанный фильм");
        }
        log.debug("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public Film findFilmById(int id) throws FilmNotFoundException {
        Film film = films.get(id);
        if (film == null) {
            throw new FilmNotFoundException("Фильма с указанным id не существует");
        }
        return film;
    }

}
