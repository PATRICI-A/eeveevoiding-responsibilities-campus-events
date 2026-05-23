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

/**
 * MapStruct mapper for converting between Event domain entities and DTOs.
 * <p>
 * Handles conversions for:
 * <ul>
 *   <li>{@link EventRequest} → {@link Event} (domain creation)</li>
 *   <li>{@link Event} → {@link EventResponse} (full response)</li>
 *   <li>{@link Event} → {@link EventFeedResponse} (feed/list response)</li>
 * </ul>
 * Also provides utility methods for time parsing and ID conversions.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

    /**
     * Converts an event creation request to a domain Event entity.
     * <p>
     * The following fields are ignored and must be set separately:
     * id, organizerId, status, availableCapacity, qrCode.
     * </p>
     *
     * @param request the event creation request DTO
     * @return the domain Event entity with basic fields populated
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizerId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "availableCapacity", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    @Mapping(target = "durationMinutes", source = "duration")
    Event toDomain(EventRequest request);

    /**
     * Converts a domain Event entity to a full event response DTO.
     *
     * @param event the domain Event entity
     * @return the full event response DTO
     */
    @Mapping(target = "duration", source = "durationMinutes")
    EventResponse toDTO(Event event);

    /**
     * Converts a domain Event entity to a lightweight feed response DTO.
     * <p>
     * Used for event listings and agenda views where detailed
     * information is not required.
     * </p>
     *
     * @param event the domain Event entity
     * @return the feed response DTO
     */
    EventFeedResponse toFeedDTO(Event event);

    /**
     * Converts a time string in "HH:mm" format to a LocalTime object.
     *
     * @param startTime the time string (format: HH:mm, e.g., "08:30")
     * @return the parsed LocalTime, or null if input is null
     * @throws EventDomainException if the time format is invalid
     */
    default LocalTime stringToLocalTime(String startTime) {
        try {
            return startTime != null
                    ? LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
                    : null;
        } catch (DateTimeParseException e) {
            throw new EventDomainException("Invalid time format, expected HH:mm (e.g. 08:30)");
        }
    }

    /**
     * Converts an EventId value object to its primitive UUID.
     *
     * @param eventId the EventId value object
     * @return the UUID value, or null if input is null
     */
    default UUID eventIdToUUID(EventId eventId) {
        return eventId != null ? eventId.getValue() : null;
    }

    /**
     * Converts a primitive UUID to an EventId value object.
     *
     * @param id the UUID value
     * @return the EventId wrapper, or null if input is null
     */
    default EventId uuidToEventId(UUID id) {
        return id != null ? new EventId(id) : null;
    }
}