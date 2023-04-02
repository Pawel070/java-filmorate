package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class FilmorateApplication {

    public static final LocalDateTime localDateTimeMinFilm = LocalDateTime.of(1895, 12, 28, 0, 0);

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
