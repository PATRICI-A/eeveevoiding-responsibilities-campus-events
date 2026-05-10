package edu.eci.patricia.DOWS_patricia.application.mapper;


import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "id", target = "id")
    EventResponse toDTO(Event event);

    @Mapping(source = "id", target = "id")
    Event toEntity(EventResponse dto);

    default String eventIdToString(EventId eventId) {
        return eventId != null ? eventId.getValue() : null;
    }

    default EventId stringToEventId(String id) {
        return id != null ? new EventId(id) : null;
    }
}