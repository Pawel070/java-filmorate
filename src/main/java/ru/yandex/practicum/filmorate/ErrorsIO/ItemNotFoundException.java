package ru.yandex.practicum.filmorate.ErrorsIO;

import java.io.Serial;

public class ItemNotFoundException extends RuntimeException {  // http:500 Аргумент метода недействителен, возникло исключение

    @Serial
    private static final long serialVersionUID = -1332224933844454151L;

    public ItemNotFoundException(String message) {
        super(message);
    }

}
