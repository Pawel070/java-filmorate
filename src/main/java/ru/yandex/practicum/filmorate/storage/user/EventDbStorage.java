package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Primary
@Slf4j
@Component
@Data
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createEvent(Event event) {
        String sqlNewEvent = "INSERT INTO EVENTS(USER_ID, TIMESTAMP, ENTITY_ID, EVENT_TYPE, OPERATION) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sqlNewEvent, event.getUserId(), event.getTimestamp(), event.getEntityId(),
                event.getEventType(), event.getOperation());
    }

    @Override
    public List<Event> getEvent(int id) {
        String sqlGetEvent = "SELECT * FROM EVENTS WHERE USER_ID = ?";
        return jdbcTemplate.query(sqlGetEvent, this::mapToRowEvent, id);
    }

    protected Event mapToRowEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("EVENT_ID"))
                .userId(rs.getInt("USER_ID"))
                .timestamp(rs.getLong("timestamp"))
                .entityId(rs.getInt("ENTITY_ID"))
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .operation(EventOperation.valueOf(rs.getString("OPERATION")))
                .build();
    }
}
