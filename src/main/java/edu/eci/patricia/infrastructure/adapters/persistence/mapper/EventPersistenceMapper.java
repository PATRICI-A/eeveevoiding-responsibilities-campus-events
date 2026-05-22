package edu.eci.patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.mapstruct.Mapper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventPersistenceMapper {

    EventEntity toEntity(Event event);

    Event toModel(EventEntity entity);

    default UUID eventIdToUUID(EventId eventId) {
        return eventId != null ? eventId.getValue() : null;
    }

    default EventId uuidToEventId(UUID id) {
        return id != null ? new EventId(id) : null;
    }

    default String localTimeToString(LocalTime startTime) {
        return startTime != null
                ? startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                : null;
    }

    default LocalTime stringToLocalTime(String startTime) {
        return startTime != null
                ? LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
                : null;
    }
}