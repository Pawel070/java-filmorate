package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@Validated
public class User {

    private int id;

    @NotBlank(message = "E07 Логин не может быть пустым или содержать пробелы.")
    private String login;

    @NotNull
    @Email(message = "E08 Электронная почта не может быть пустой и должна содержать символ @.")
    private final String email;

    @Past(message = "E10 Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }
        if (id != user.id) {
            return false;
        }
        if (!Objects.equals(login, user.login)) {
            return false;
        }
        if (!email.equals(user.email)) {
            return false;
        }
        if (!Objects.equals(birthday, user.birthday)) {
            return false;
        }
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + email.hashCode();
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}

