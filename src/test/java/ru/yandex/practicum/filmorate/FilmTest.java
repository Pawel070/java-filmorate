package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;


public class FilmTest {

    @Test
    public void createFilmTest() {
        Set<Long> like = new HashSet<>();
        List<Genre> genres = new ArrayList<>();
        Film.builder()
                .idFilm(1)
                .name("Фильм")
                .description("Описание")
                .releaseDate(LocalDate.of(1995, 12, 28))
                .duration(50)
                .likes(like)
                .genre(genres)
                .idRate(1)
                .build();
    }

}
