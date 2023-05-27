package ru.yandex.practicum.filmorate.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EventService {

    @Autowired
    private EventStorage eventStorage;

    public List<Event> getEvent(int id) {
        return eventStorage.getEvent(id);
    }

    public void createEvent(int userId, EventType eventType, EventOperation operation, int entityId) {
        Event event = Event.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();

        eventStorage.createEvent(event);
    }

}
