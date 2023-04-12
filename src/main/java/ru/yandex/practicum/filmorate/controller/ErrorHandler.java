package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.ErrorsIO.IncorrectParameterException;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleIncorrectParameterException(final IncorrectParameterException e) {
        log.info("Ошибка значения поля. Возврат код 400 {}", e.getMessage());
        return new ErrorResponse(String.format("http:400 Ошибка с полем \"%s\".", e.getParameter()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.info("Ошибка валидации. Возврат код 400 {}", e.getMessage());
        return new ErrorResponse("http:400 Ошибка валидации.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorResponse handleMethodArgumentNotException(final MethodArgumentNotException e) {
        log.info("Искомый объект не найден. Возврат код 404 {}", e.getMessage());
        return new ErrorResponse("http:404 Искомый объект не найден.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("Искомый объект не найден. Возврат код 400 {}", e.getMessage());
        return new ErrorResponse("http:400 Искомый объект не найден при первичной проверке.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info("Возникло исключение. Возврат код 500 {}", e.getMessage());
        return new ErrorResponse("http:500 Аргумент метода недействителен, возникло исключение", e.getMessage());
    }
}

