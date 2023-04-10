package ru.yandex.practicum.filmorate;

import lombok.extern.java.Log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log
@SpringBootApplication
public class FilmorateApplication {


    public static void main(String[] args) {
        log.info("Starting application.");
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
