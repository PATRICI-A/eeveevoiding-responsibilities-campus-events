package edu.eci.patricia.application.usecase;

import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.RsvpAlreadyExistsException;
import edu.eci.patricia.domain.exceptions.RsvpNotFoundException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.EventRsvp;
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

    @Override
    public void execute(UUID eventId, UUID studentId) {

        EventId evId = new EventId(eventId);

        Event event = eventRepository.findById(evId)
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));

        boolean rsvp = rsvpRepository.existsByEventIdAndStudentId(evId, studentId);

        if (rsvp.getStatus() == RsvpStatus.CANCELLED) {
            throw new RsvpAlreadyExistsException("RSVP is already cancelled for event: " + eventId);
        }

        rsvp.setStatus(RsvpStatus.CANCELLED);
        rsvpRepository.save(rsvp);

        if (event.getType() == EventType.WITH_CAPACITY) {
            event.setAvailableCapacity(event.getAvailableCapacity() + 1);
            eventRepository.save(event);
        }
    }
}