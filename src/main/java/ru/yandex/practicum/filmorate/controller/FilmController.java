package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final Map<Integer, Film> films = new HashMap<>();
    private static int newId = 1;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (checkFilm(film)) {
            film.setId(newId++);
            log.debug("Добавлен фильм: {}", film);
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Проверьте корректность введенных данных!");
        }

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (checkFilm(film)) {
            if (films.containsKey(film.getId())) {
                log.debug("Обновлен фильм: {}", film);
                films.put(film.getId(), film);
            } else {
                throw new ValidationException("Проверьте корректность введенных данных");
            }
        } else {
            throw new ValidationException("Проверьте корректность введенных данных!");
        }
        return film;
    }

    private boolean checkFilm(Film film) {
        if (film.getName().isEmpty()) {
            log.debug("Имя фильма пустое");
            return false;
        } else if (film.getDescription().length() > 200) {
            log.debug("Слишком длинное описание");
            return false;
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 1, 28))) {
            log.debug("Некорректная дата фильма");
            return false;
        } else if (film.getDuration() < 0) {
            log.debug("Отрицательная продолжительность фильма");
            return false;
        }
        return true;
    }

}
