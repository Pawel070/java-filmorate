package ru.yandex.practicum.filmorate.ErrorsIO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MethodArgumentNotException extends RuntimeException { // http:404 Искомый объект не найден.
    private String parameter;

    public MethodArgumentNotException(String parameter) {
        log.info("Искомый объект не найден > {}", parameter);
        this.parameter = parameter;
    }

}

