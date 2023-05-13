package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import ru.yandex.practicum.filmorate.dataService.RateData;

@Data
@Builder
public class Rating {

    private int idRate;
    private RateData idRateDate;
    private String rate;

    public Rating(int idRate, RateData idRateDate, String rate) {
        this.idRate = idRate;
        this.idRateDate = idRateDate;
        this.rate = rate;
    }

}
