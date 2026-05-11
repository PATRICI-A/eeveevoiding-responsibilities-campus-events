package edu.eci.patricia.DOWS_patricia.application.mapper;


import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;


@Mapper(componentModel = "spring")
public interface EventRsvpMapper {

    @Mapping(source = "id", target = "id")
    EventResponseRsvp toDTO(EventRsvp eventRsvp);

    @Mapping(source = "id", target = "id")
    EventRsvp toModel(EventResponseRsvp dto);

    default UUID rsvpIdToString(RsvpId rsvpId) {
        return rsvpId != null ? rsvpId.getValue() : null;
    }

    default RsvpId stringToRsvpId(UUID id) {
        return id != null ? new RsvpId(id) : null;
    }

}