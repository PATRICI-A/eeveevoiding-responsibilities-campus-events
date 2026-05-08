package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.StudentId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import org.springframework.stereotype.Component;

@Component
public class EventRsvpPersistenceMapper {

    public EventRsvpEntity toEntity(EventRsvp rsvp) {
        return EventRsvpEntity.builder()
                .id(rsvp.getId().getValue())
                .eventId(rsvp.getEventId().getValue())
                .studentId(rsvp.getStudentId().getValue())
                .confirmedAt(rsvp.getConfirmedAt())
                .status(rsvp.getStatus())
                .build();
    }

    public EventRsvp toDomain(EventRsvpEntity entity) {
        return EventRsvp.builder()
                .id(new RsvpId(entity.getId()))
                .eventId(new EventId(entity.getEventId()))
                .studentId(new StudentId(entity.getStudentId()))
                .confirmedAt(entity.getConfirmedAt())
                .status(entity.getStatus())
                .build();
    }
}
