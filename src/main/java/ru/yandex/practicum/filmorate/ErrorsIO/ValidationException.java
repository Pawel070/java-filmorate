package ru.yandex.practicum.filmorate.ErrorsIO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ValidationException extends RuntimeException {
    private String parameter;

    public ValidationException(String parameter) {
        log.info("Ошибка валидации > {}", parameter);
        this.parameter = parameter;
    }

}

