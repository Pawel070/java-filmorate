package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserTest {

    @Test
    public void createUserTest() {
        User.builder()
                .idUser(1)
                .name("Имя")
                .birthday(LocalDate.of(1995, 12, 28))
                .email("test@qq.ru")
                .login("Логин")
                .build();
    }
}
