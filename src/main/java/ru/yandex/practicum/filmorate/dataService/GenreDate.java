package ru.yandex.practicum.filmorate.dataService;

public enum GenreDate {
    COMEDY ("Комедия"),
    DRAMA ("Драма"),
    CARTOON ("Мультфильм"),
    TRILLER ("Триллер"),
    DOCUMENTARY ("Документальный"),
    HITMAN ("Боевик"),
    INDISTINCT("Неясный"); // неясный по умолчанию

    private String title;

    GenreDate(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

}
