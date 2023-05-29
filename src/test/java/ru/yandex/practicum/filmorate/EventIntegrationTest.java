package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.EventDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class EventIntegrationTest {

    private final UserService userService;
    private final UserDbStorage userStorage;
    private final EventDbStorage eventDbStorage;

    @Test
    public void testGetEventEmpty() {
        User user = User.builder()
                .name("name")
                .email("name@ya.ru")
                .login("login")
                .birthday(LocalDate.parse("2000-06-06"))
                .build();
        userStorage.create(user);

        List<Event> events = userService.getEvent(1);
        assertEquals(events.size(), 0);
        assertTrue(eventDbStorage.getEvent(1).isEmpty());
    }

    @Test
    public void testGetEventWithEvents() {

        User user = User.builder()
                .name("name")
                .email("name@ya.ru")
                .login("login")
                .birthday(LocalDate.parse("2000-06-06"))
                .build();
        userStorage.create(user);

        Event event = Event.builder()
                .userId(1)
                .entityId(1)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(EventType.FRIEND)
                .operation(EventOperation.ADD)
                .build();

        eventDbStorage.createEvent(
                event.getUserId(),
                event.getEventType(),
                event.getOperation(),
                event.getEntityId(),
                event.getTimestamp()
        );

        event.setEventId(1);
        assertTrue(eventDbStorage.getEvent(2).contains(event));
    }
}
