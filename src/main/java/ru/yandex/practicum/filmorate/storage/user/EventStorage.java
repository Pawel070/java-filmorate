package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {
    public void createEvent(Event event);
    List<Event> getEvent(int id);
}
