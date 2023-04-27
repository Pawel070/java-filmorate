package ru.yandex.practicum.filmorate.ErrorsIO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MethodArgumentNotValidException extends RuntimeException {
    private String parameter;

    public MethodArgumentNotValidException(String parameter) {
        log.info("Искомый объект не найден > {}", parameter);
        this.parameter = parameter;
    }

}

