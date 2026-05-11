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

    @Mapping(source = "id", target = "id")
    EventResponse toDTO(Event event);

    @Mapping(source = "id", target = "id")
    Event toModel(EventRequest dto);

    default UUID eventIdToString(EventId eventId) {
        return eventId != null ? eventId.getValue() : null;
    }

    default EventId stringToEventId(UUID id) {
        return id != null ? new EventId(id) : null;
    }
}