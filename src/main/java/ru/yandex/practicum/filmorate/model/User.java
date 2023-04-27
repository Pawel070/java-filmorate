package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Data
@Validated
@Builder
public class User {

    private int id;

    @NotBlank(message = "E07 Логин не может быть пустым или содержать пробелы.")
    private String login;

    @NotNull
    @Email(message = "E08 Электронная почта не может быть пустой и должна содержать символ @.")
    private String email;

    @Past(message = "E10 Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    private String name;

    private Set<Long> friends; //  id друзей

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}

