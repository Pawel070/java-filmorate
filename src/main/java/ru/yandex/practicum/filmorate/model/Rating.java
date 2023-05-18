package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rating {

    private int idRate;
    private String idRateDate;
    private String rate;

    public Rating(int idRate, String idRateDate, String rate) {
        this.idRate = idRate;
        this.idRateDate = idRateDate;
        this.rate = rate;
    }

}
