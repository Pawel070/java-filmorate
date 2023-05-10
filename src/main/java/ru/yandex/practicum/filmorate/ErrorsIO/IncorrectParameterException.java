package ru.yandex.practicum.filmorate.ErrorsIO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class IncorrectParameterException extends RuntimeException {

    private final String parameter;

    public IncorrectParameterException(String parameter) {
        log.info("Ошибка с полем \"%s\".", parameter);
        this.parameter = parameter;
    }

}