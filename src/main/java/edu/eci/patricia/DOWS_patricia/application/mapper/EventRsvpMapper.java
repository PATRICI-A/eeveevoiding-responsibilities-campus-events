package edu.eci.patricia.DOWS_patricia.application.mapper;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventRsvpMapper {

    public EventRsvp toDomain(EventRequestRsvp request) {
        return EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(new EventId(request.getEventId()))
                .studentId(new StudentId(request.getStudentId()))
                .confirmedAt(LocalDateTime.now())
                .status(request.getStatus())
                .build();
    }

    public EventResponseRsvp toResponse(EventRsvp rsvp) {
        return EventResponseRsvp.builder()
                .id(rsvp.getId().getValue())
                .eventId(rsvp.getEventId().getValue())
                .studentId(rsvp.getStudentId().getValue())
                .confirmedAt(rsvp.getConfirmedAt())
                .status(rsvp.getStatus())
                .build();
    }
}