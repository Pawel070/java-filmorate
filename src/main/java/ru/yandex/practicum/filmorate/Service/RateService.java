package ru.yandex.practicum.filmorate.Service;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.RateDbStorage;

@Slf4j
@Service
public class RateService {

    private final RateDbStorage storage;

    @Autowired
    public RateService(RateDbStorage storage) {
        this.storage = storage;
    }

    public Collection<Rating> findAll() {
        log.info("Возвращаем список рейтингов");
        return storage.getRate();
    }

    public Rating findById(int id) {
        log.info("Возвращаем рейтинг с id {} ", id);
        Rating rating;
        try {
            rating = storage.checkRate(id);
        } catch (Exception e) {
            log.info("Рейтинга с id {} нет", id);
            throw new MethodArgumentNotException("Рейтинга с id " + id + " нет.");
        }

        return rating;
    }

}
