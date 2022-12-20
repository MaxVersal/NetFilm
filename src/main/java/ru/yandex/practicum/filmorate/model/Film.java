package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @NonNull
    int id;

    @NonNull
    String name;

    @NonNull
    String description;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;

    @NonNull
    int duration;

    Set<Integer> likes = new HashSet<>();
}
