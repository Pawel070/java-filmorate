package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.Instant;
import java.util.List;

//@Primary
//@Slf4j
@Component
@Qualifier
//@Data
//@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createEvent(int userId, EventType eventType, EventOperation operation, int entityId, long timestamp) {

        String sqlNewEvent = "INSERT INTO FILMORATE_SHEMA.EVENTS(USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID, TIMESTAMP) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlNewEvent, userId, eventType.toString(), operation.toString(), entityId, timestamp);
       // jdbcTemplate.update(sqlNewEvent, event.getUserId(), event.getEventType().name(),event.getOperation().name(),
         //       event.getEntityId(), event.getTimestamp());
    }


    @Override
    public List<Event> getEvent(int id) {
        String sqlGetEvent = "SELECT * FROM FILMORATE_SHEMA.EVENTS WHERE USER_ID = ?";
        return jdbcTemplate.query(sqlGetEvent, this::mapToRowEvent, id);
    }

    private Event mapToRowEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("EVENT_ID"))
                .userId(rs.getInt("USER_ID"))
                .operation(EventOperation.valueOf(rs.getString("OPERATION")))
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .entityId(rs.getInt("ENTITY_ID"))
                .timestamp(rs.getLong("timestamp"))
                .build();
    }
}
