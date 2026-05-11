package edu.eci.patricia.DOWS_patricia.application.mapper;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventRsvpMapper {

    EventResponseRsvp toDTO(EventRsvp eventRsvp);

    default UUID rsvpIdToUUID(RsvpId rsvpId) {
        return rsvpId != null ? rsvpId.getValue() : null;
    }

    default RsvpId uuidToRsvpId(UUID id) {
        return id != null ? new RsvpId(id) : null;
    }

    default UUID eventIdToUUID(EventId eventId) {
        return eventId != null ? eventId.getValue() : null;
    }

    default EventId uuidToEventId(UUID id) {
        return id != null ? new EventId(id) : null;
    }
}