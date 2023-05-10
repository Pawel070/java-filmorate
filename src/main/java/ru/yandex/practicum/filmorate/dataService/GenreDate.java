package ru.yandex.practicum.filmorate.dataService;

public enum GenreDate {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    TRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    HITMAN(6, "Боевик"),
    INDISTINCT(7, "Неясный"); // неясный по умолчанию

    private final String title;

    GenreDate(int idGenre, String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

}
