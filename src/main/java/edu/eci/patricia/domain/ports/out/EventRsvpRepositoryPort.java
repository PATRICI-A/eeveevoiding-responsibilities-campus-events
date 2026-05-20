package edu.eci.patricia.domain.ports.out;

import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.valueobjects.EventId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRsvpRepositoryPort {

    EventRsvp save(EventRsvp rsvp);
    boolean existsByEventIdAndStudentId(EventId eventId, UUID studentId);
    List<EventRsvp> findConfirmedByStudentId(UUID studentId);
    List<EventRsvp> findConfirmedByEventId(EventId eventId);
    Optional<EventRsvp> findByEventIdAndStudentId(UUID eventId, UUID studentId);

}