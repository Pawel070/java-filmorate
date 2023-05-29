package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

import java.util.List;

public interface EventStorage {
    void createEvent(int userId, EventType eventType, EventOperation operation, int entityId, long timestamp);

    List<Event> getEvent(int id);
}
