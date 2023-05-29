package ru.yandex.practicum.filmorate.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.storage.user.EventStorage;

import java.time.Instant;
import java.util.List;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventStorage eventStorage;

    public List<Event> getEvent(int id) {
        return eventStorage.getEvent(id);
    }

    public void createEvent(int userId, int entityId, EventType eventType, EventOperation operation) {
        log.info("Добавление в ленту операции {} типа {} от пользователя с id {}", operation, eventType, userId);
        long timestamp = Instant.now().toEpochMilli();
        eventStorage.createEvent(userId, eventType, operation, entityId, timestamp);
    }
}
