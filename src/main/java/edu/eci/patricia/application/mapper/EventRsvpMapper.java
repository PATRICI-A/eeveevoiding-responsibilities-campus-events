package edu.eci.patricia.application.mapper;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import org.mapstruct.Mapper;

import java.util.UUID;

/**
 * MapStruct mapper for converting between EventRsvp domain entities and DTOs.
 * <p>
 * Provides conversion methods for the RSVP response DTO and utility methods
 * for converting between value objects (RsvpId, EventId) and primitive UUIDs.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface EventRsvpMapper {

    /**
     * Converts a domain EventRsvp entity to a response DTO.
     *
     * @param eventRsvp the domain RSVP entity
     * @return the RSVP response DTO
     */
    EventResponseRsvp toDTO(EventRsvp eventRsvp);

    /**
     * Converts an RsvpId value object to its primitive UUID.
     *
     * @param rsvpId the RsvpId value object
     * @return the UUID value, or null if input is null
     */
    default UUID rsvpIdToUUID(RsvpId rsvpId) {
        return rsvpId != null ? rsvpId.getValue() : null;
    }

    /**
     * Converts a primitive UUID to an RsvpId value object.
     *
     * @param id the UUID value
     * @return the RsvpId wrapper, or null if input is null
     */
    default RsvpId uuidToRsvpId(UUID id) {
        return id != null ? new RsvpId(id) : null;
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