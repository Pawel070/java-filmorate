package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;


public class FilmTest {

    @Test
    public void createFilmTest() {
        Rating rating = new Rating(1, "");
        Set<Long> like = new HashSet<>();
        List<Genre> genres = new ArrayList<>();
        Film.builder()
                .id(1)
                .name("Фильм01")
                .description("Описание01")
                .releaseDate(LocalDate.of(1995, 12, 28))
                .duration(50)
                .likes(like)
                .genres(genres)
                .mpa(rating)
                .build();
    }

}
