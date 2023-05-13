package ru.yandex.practicum.filmorate.dataService;

public enum FriendsData {
    FRIEND("ДРУГ"),
    NO_FRIEND("НЕДРУГ"),
    FRIEND_REQUEST("ЗАПРОС ДРУЗЕЙ");

    private final String title;

    FriendsData(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

}

