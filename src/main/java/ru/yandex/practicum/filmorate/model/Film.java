package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDateTime releaseDate;
    private long duration;
}

