package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {

    @NonNull
    Integer id;


    String name;
}
