package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController controller;

    private static Gson gson;
    private static String filmString;

    @BeforeAll
    static void init() {
        gson = new Gson();
        filmString = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"description\": \"adipisicing\",\n" +
                "  \"releaseDate\": \"1967-03-25\",\n" +
                "  \"duration\": 100,\n" +
                "  \"mpa\": { \"id\": 1}\n" +
                "}";
    }

    @Test
    void contextLoads() throws Exception {
        this.mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnErrorWithoutBody() throws Exception {
        this.mockMvc.perform(post("/films"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldPostFilm() throws Exception {
        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(filmString))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldPutFilm() throws Exception {
        this.mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(filmString));
        this.mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"Film Updated\",\n" +
                                "  \"releaseDate\": \"1989-04-17\",\n" +
                                "  \"description\": \"New film update decription\",\n" +
                                "  \"duration\": 190,\n" +
                                "  \"rate\": 4,\n" +
                                "  \"mpa\": { \"id\": 2}\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
