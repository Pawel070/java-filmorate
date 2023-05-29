package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Event {
    private int eventId;
    private int userId;
    private long timestamp;
    private int entityId;
    private EventType eventType;
    private EventOperation operation;
}
