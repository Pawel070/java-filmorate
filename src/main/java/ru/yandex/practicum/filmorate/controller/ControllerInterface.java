package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;

public interface ControllerInterface<T> {

    T create(T t);

    T update(T t);

    Collection<T> selectGetting();

    T getById(int id);

    void delete(int id);

}