package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@Validated
public class User {
    @NotBlank(message = "E12 Имя не должно быть пустым. Используйте Логин.")
    private String name;
    private int id;
    @NotBlank(message = "E07 Логин не может быть пустым или содержать пробелы.")
    private String login;
    @NotNull @Email(message = "E08 Электронная почта не может быть пустой и должна содержать символ @.")
    private String email;
    @Past(message = "E10 Дата рождения не может быть в будущем.")
    private LocalDateTime birthday;

}

