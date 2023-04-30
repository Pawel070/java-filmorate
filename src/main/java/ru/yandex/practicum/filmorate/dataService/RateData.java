package ru.yandex.practicum.filmorate.dataService;

public enum RateData {
    G ("G"),       //у фильма нет возрастных ограничений,
    PG ("PG"),      // детям рекомендуется смотреть фильм с родителями,
    PG14 ("PG-13"), // детям до 13 лет просмотр не желателен,
    R ("R"),       // лицам до 17 лет просматривать фильм можно только в присутствии взрослого,
    NC17 ("NC-17");   // лицам до 18 лет просмотр запрещён.

    private String title;

    RateData(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
