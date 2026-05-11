package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventRsvpPersistenceMapper {

    EventRsvpEntity toEntity(EventRsvp eventRsvp);

    EventRsvp toModel(EventRsvpEntity entity);

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