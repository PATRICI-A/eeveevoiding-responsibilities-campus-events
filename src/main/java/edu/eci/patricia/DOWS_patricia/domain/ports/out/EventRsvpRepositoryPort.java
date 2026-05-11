package edu.eci.patricia.DOWS_patricia.domain.ports.out;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRsvpRepositoryPort {

    EventRsvp save(EventRsvp rsvp);
    Optional<EventRsvp> findByEventIdAndStudentId(EventId eventId, UUID studentId);
    List<EventRsvp> findConfirmedByStudentId(UUID studentId);
    List<EventRsvp> findConfirmedByEventId(EventId eventId);
}