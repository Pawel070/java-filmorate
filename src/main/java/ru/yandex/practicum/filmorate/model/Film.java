package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@Validated
public class Film {
    private int id;
    @NotBlank(message = "E01 Название фильма не может быть пустым.")
    private String name;
    @Size(min = 1, max = 200, message = "E11 Длина описания не более 200 символов.")
    private String description;
    private LocalDateTime releaseDate;
    @Min(value = 1, message = "E03 Продолжительность фильма должна быть положительной и не 0.")
    private long duration;
}

