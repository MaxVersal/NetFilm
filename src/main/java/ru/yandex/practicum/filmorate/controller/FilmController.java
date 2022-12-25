package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private  final FilmService filmService;

    @Autowired
    public FilmController (FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id,@PathVariable int userId) throws FilmNotFoundException, UserNotFoundException {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) throws FilmNotFoundException, UserNotFoundException {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> popularFilms(@RequestParam (defaultValue = "10", required = false) Integer count){
        return filmService.popularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) throws FilmNotFoundException{
        return filmService.searchFilmById(id);
    }

}
