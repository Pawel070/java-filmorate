package ru.yandex.practicum.filmorate.ErrorsIO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ValidationException extends RuntimeException {
    private String s;

    public ValidationException(String s) {
         log.info("ValidationException > {}", s);
        this.s = s;
    }

}

