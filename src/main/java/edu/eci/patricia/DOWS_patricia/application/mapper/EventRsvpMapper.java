package edu.eci.patricia.DOWS_patricia.application.mapper;


import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface EventRsvpMapper {


    @Mapping(source = "id", target = "id")
    EventResponseRsvp toDTO(EventRsvp eventRsvp);

    @Mapping(source = "id", target = "id")
    EventRsvp toEntity(EventResponseRsvp dto);

    default String rsvpIdToString(RsvpId rsvpId) {
        return rsvpId != null ? rsvpId.getValue() : null;
    }

    default RsvpId stringToRsvpId(String id) {
        return id != null ? new RsvpId(id) : null;
    }

}