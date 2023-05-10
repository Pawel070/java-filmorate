package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Validated
@Builder
public class Film {

    private int idFilm;

    @NotBlank(message = "E01 Название фильма не может быть пустым.")
    private String name;

    @Size(min = 1, max = 200, message = "E11 Длина описания не более 200 символов.")
    private String description;

    private LocalDate releaseDate;

    @Min(value = 1, message = "E03 Продолжительность фильма должна быть положительной и не 0.")
    private long duration;

    private Set<Long> likes; //  список id лайкающих

    private List<Genre> genre; //  vмассив жанров

    @NotNull
    private int idRate;  // рейтинг

}

