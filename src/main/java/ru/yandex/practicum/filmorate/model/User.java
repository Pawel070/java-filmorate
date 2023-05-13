package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Validated
@Builder
public class User {

    private int idUser;

    @NotBlank(message = "E07 Логин не может быть пустым или содержать пробелы.")
    private String login;

    @NotNull
    @Email(message = "E08 Электронная почта не может быть пустой и должна содержать символ @.")
    private final String email;

    @Past(message = "E10 Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    private String nameUser;

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof User user && Objects.equals(email, user.email);

    }

    @Override
    public int hashCode() {
        if (email != null) {
            return email.hashCode();
        }
        return 0;
    }
}

