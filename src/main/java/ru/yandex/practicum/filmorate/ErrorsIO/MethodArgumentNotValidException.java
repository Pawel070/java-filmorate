package ru.yandex.practicum.filmorate.ErrorsIO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MethodArgumentNotValidException extends RuntimeException {
    private String s;

    public MethodArgumentNotValidException(String s) {
        log.error("MethodArgumentNotValidException > {}", s);
        this.s = s;
    }

}

