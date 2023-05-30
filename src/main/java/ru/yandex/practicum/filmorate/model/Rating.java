package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rating {

    private int id;
    private String name;

    public Rating(int idRate, String name) {
        this.id = idRate;
        this.name = name;
    }

}
