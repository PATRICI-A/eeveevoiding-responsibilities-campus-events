package edu.eci.patricia.DOWS_patricia.domain.ports.out;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryPort {

    Event save(Event event);

    Optional<Event> findById(EventId id);

    List<Event> findActiveEvents(EventCategory category, LocalDate date);

    List<Event> findByStatus(EventStatus status);
}