package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class GlobalAbstractBon {

    @NotNull
    private long id;
}

