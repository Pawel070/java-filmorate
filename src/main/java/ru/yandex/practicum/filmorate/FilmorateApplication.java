package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FilmorateApplication {


    public static void main(String[] args) {
        log.info("\n\nСтарт Приложения. Log-файл > target/filmarate.log. \n");
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
