package ru.yandex.practicum.filmorate.ErrorsIO;

import lombok.Data;

@Data
public class ValidationException extends RuntimeException {
    private String s;

    public ValidationException(String s) {
        this.s = s;
    }
}

