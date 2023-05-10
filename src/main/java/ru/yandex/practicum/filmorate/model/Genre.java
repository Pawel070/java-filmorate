package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {

    private int idGenre;
    private String idGenreDate;
    private String genreRus;

    public Genre(int idGenre, String idGenreDate, String genreRus) {
        this.idGenre = idGenre;
        this.idGenreDate = idGenreDate;
        this.genreRus = genreRus;
    }

}
