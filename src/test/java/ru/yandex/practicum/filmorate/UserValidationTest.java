package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidationTest {
    private static UserController controller;
    private static Gson gson;
    private static User user;

    @BeforeAll
    public static void init() {
        controller = new UserController();
        gson = new Gson();
        user = new User(1,
                "asd@mail.ru",
                "ax",
                "a",
                LocalDate.of(2002, 1, 20));
    }

    @Test
    public void shouldReturnTrue(){
        Assertions.assertTrue(controller.checkUser(user));
    }

    @Test
    public void shouldReturnFalse(){
        User incorrectUser = new User(1,
                "as.",
                "ax",
                "a",
                LocalDate.of(2999, 1, 2));
        Assertions.assertFalse(controller.checkUser(incorrectUser));
    }
}
