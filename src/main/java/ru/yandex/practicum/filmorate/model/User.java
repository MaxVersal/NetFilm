package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @NonNull
    int id;

    @NonNull
    String email;

    @NonNull
    String login;

    @NonNull
    String name;

    @NonNull
    LocalDate birthday;

    Set<Integer> friends = new HashSet<>();
}
