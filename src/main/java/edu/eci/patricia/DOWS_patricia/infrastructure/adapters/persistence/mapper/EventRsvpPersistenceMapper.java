package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventEntity;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EventRsvpPersistenceMapper {


    @Mapping(source = "id", target = "id")
    EventRsvpEntity toEntity(EventRsvp eventRsvp);

    @Mapping(source = "id", target = "id")
    EventRsvp toModel(EventRsvpEntity entity);


    default UUID eventIdToUUID(RsvpId rsvpId) {
        return rsvpId != null ? UUID.fromString(rsvpId.getValue()) : null;
    }

    default RsvpId uuidToEventId(UUID id) {
        return id != null ? new RsvpId(id.toString()) : null;
    }
}
