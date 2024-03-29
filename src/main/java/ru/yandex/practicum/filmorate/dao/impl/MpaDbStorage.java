package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllRatings() {
        List<Mpa> mpa = jdbcTemplate.query("select * from MPA", new BeanPropertyRowMapper<>(Mpa.class));
        for (Mpa mpa1 : mpa) {
            mpa1.setId(jdbcTemplate.queryForObject("select rating_id from mpa where name = ?",
                    new Object[]{mpa1.getName()},
                    Integer.class));
        }
        return mpa;
    }

    @Override
    public Mpa getRatingById(int id) throws MpaNotFoundException {
        SqlRowSet sql = jdbcTemplate.queryForRowSet("select * from MPA where RATING_ID = ?", id);
        if (sql.next()) {
            Mpa mpa = new Mpa(
                    sql.getInt("rating_id"),
                    sql.getString("name")
            );
            return mpa;
        } else {
            throw new MpaNotFoundException("Нет рейтинга с данным id");
        }
    }
}
