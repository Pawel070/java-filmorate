package ru.yandex.practicum.filmorate.storage.film;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.Rating;

public interface RateStorage {

    Collection<Rating> getRate();

    Rating checkRate(int idRate);

}
