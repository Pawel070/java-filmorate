package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.GlobalAbstractBon;

public class GlobalAbstractStorage<T extends GlobalAbstractBon>
        extends GlobalAbstractInMemoryStorage<T> {
    @Override
    void validate(T item) throws ValidationException {

    }
}
