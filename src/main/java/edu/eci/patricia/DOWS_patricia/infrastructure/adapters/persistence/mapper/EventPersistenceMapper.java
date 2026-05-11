package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.mapstruct.Mapper;

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
}