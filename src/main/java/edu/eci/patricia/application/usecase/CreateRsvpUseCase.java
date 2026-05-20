package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.exceptions.EventCapacityFullException;
import edu.eci.patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.RsvpAlreadyExistsException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRsvpUseCase implements CreateRsvpPort {

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

        if (rsvpRepository.existsByEventIdAndStudentId(evId, studentId) && ){

        }



        if (event.getType() == EventType.WITH_CAPACITY &&
                (event.getAvailableCapacity() == null || event.getAvailableCapacity() <= 0)) {
            throw new EventCapacityFullException(eventId.toString());
        }

        EventRsvp rsvp = EventRsvp.builder()
                .id(new RsvpId(UUID.randomUUID()))
                .eventId(evId)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        if (event.getType() == EventType.WITH_CAPACITY) {
            event.setAvailableCapacity(event.getAvailableCapacity() - 1);
            eventRepository.save(event);
        }

        return rsvpMapper.toDTO(rsvpRepository.save(rsvp));
    }
}