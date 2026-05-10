package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventPersistenceMapper {

    @Mapping(source = "id", target = "id")
    EventEntity toEntity(Event event);

    @Mapping(source = "id", target = "id")
    Event toModel(EventEntity entity);


    default UUID eventIdToUUID(EventId eventId) {
        return eventId != null ? UUID.fromString(eventId.getValue()) : null;
    }

    default EventId uuidToEventId(UUID id) {
        return id != null ? new EventId(id.toString()) : null;
    }


}