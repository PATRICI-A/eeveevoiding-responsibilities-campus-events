package edu.eci.patricia.application.mapper;

import edu.eci.patricia.application.dto.request.EventRequest;
import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.domain.exceptions.EventDomainException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.valueobjects.EventId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizerId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "availableCapacity", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    @Mapping(target = "durationMinutes", source = "duration")
    Event toDomain(EventRequest request);

    @Mapping(target = "duration", source = "durationMinutes")
    EventResponse toDTO(Event event);

    EventFeedResponse toFeedDTO(Event event);

    default LocalTime stringToLocalTime(String startTime) {
        try {
            return startTime != null
                    ? LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
                    : null;
        } catch (DateTimeParseException e) {
            throw new EventDomainException("Invalid time format, expected HH:mm (e.g. 08:30)");
        }
    }

    default UUID eventIdToUUID(EventId eventId) {
        return eventId != null ? eventId.getValue() : null;
    }

    default EventId uuidToEventId(UUID id) {
        return id != null ? new EventId(id) : null;
    }
}