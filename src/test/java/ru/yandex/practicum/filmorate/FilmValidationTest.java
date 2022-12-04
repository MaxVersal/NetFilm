package java.ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidationTest {

    private static FilmController controller;
    private static Gson gson;
    private static String gsonFilm;

    private static Film film;

    @BeforeAll
    public static void init() {
        controller = new FilmController();
        gson = new Gson();
        film = new Film(1, "a", "asdasd", LocalDate.of(1985, 1, 30), 190);
    }

    @Test
    public void shouldReturnTrue() {
        Assertions.assertTrue(controller.checkFilm(film));
    }

    @Test
    public void shouldReturnFalse() {
        Film incorrectFilm = new Film(1,
                "a",
                "asd",
                LocalDate.of(1500, 1 ,30),
                190);
        Assertions.assertFalse(controller.checkFilm(incorrectFilm));
    }
}
