package edu.eci.patricia.DOWS_patricia.application.mapper;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizerId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "availableCapacity", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    Event toDomain(EventRequest request);

    EventResponse toDTO(Event event);

    default UUID eventIdToUUID(EventId eventId) {
        return eventId != null ? eventId.getValue() : null;
    }

    default EventId uuidToEventId(UUID id) {
        return id != null ? new EventId(id) : null;
    }
}