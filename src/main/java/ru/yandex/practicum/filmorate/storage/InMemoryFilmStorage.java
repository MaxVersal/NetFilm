package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Имя фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть длиннее 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 1, 28))) {
            throw new ValidationException("Некорректная дата фильма");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Неккоректная длительность фильма");
        }
        film.setId(newId++);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            if (film.getName().isEmpty()) {
                throw new ValidationException("Имя фильма не может быть пустым");
            } else if (film.getDescription().length() > 200) {
                throw new ValidationException("Описание не может быть длиннее 200 символов");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 1, 28))) {
                throw new ValidationException("Некорректная дата фильма");
            } else if (film.getDuration() < 0) {
                throw new ValidationException("Неккоректная длительность фильма");
            }
                films.put(film.getId(), film);
        } else {
            throw new FilmNotFoundException("Не найден указанный фильм");
        }
        log.debug("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public Film findFilmById(int id) throws FilmNotFoundException {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new FilmNotFoundException("Фильма с указанным id не существует");
        }
    }
}
