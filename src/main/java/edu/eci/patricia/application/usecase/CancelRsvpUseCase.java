package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.RsvpAlreadyExistsException;
import edu.eci.patricia.domain.exceptions.RsvpNotFoundException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelRsvpUseCase implements CancelRsvpPort {

    private final EventRepositoryPort eventRepository;
    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRsvpMapper rsvpMapper;

    @Override
    public EventResponseRsvp execute(UUID eventId, UUID studentId) {

        EventId evId = new EventId(eventId);

        Event event = eventRepository.findById(evId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (event.getStatus() != EventStatus.ACTIVE ) {
            throw new EventNotActiveException("Can´t modify no ACTIVE event");
        }

        EventRsvp existingRsvp = rsvpRepository.findByEventIdAndStudentId(eventId, studentId)
                .orElseThrow(() -> new RsvpNotFoundException("RSVP not found for this event and student"));

        if (existingRsvp.getStatus() == RsvpStatus.CANCELLED) {
            throw new RsvpAlreadyExistsException("RSVP is already cancelled for event: " + eventId);
        } else if (existingRsvp.getStatus() == RsvpStatus.CONFIRMED) {
            existingRsvp.setStatus(RsvpStatus.CANCELLED);
            if (event.getType() == EventType.WITH_CAPACITY) {
                event.setAvailableCapacity(event.getAvailableCapacity() + 1);
                eventRepository.save(event);
            }
        }

        return rsvpMapper.toDTO(rsvpRepository.save(existingRsvp));
    }
}