package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.exceptions.*;
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
import edu.eci.patricia.infrastructure.notification.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRsvpUseCase implements CreateRsvpPort {

    private final EventRepositoryPort eventRepository;
    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRsvpMapper rsvpMapper;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public EventResponseRsvp execute(UUID eventId, UUID studentId) {

        EventId evId = new EventId(eventId);

        Event event = eventRepository.findById(evId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (event.getStatus() != EventStatus.ACTIVE ) {
            throw new EventNotActiveException("Can´t modify no ACTIVE event");
        }

        if (event.getAvailableCapacity() == 0) {
            throw new EventCapacityFullException("Event capacity is FULL");
        }


        if (!rsvpRepository.existsByEventIdAndStudentId(evId, studentId)) {
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
            rsvpRepository.save(rsvp);

            LocalDateTime eventDate = LocalDateTime.of(event.getDateTime(), event.getStartTime());
            notificationServiceClient.registerEventReminder(
                    studentId,
                    eventId,
                    eventDate
            );

            return rsvpMapper.toDTO(rsvpRepository.save(rsvp));
        }

        EventRsvp existingRsvp = rsvpRepository.findByEventIdAndStudentId(eventId, studentId)
                .orElseThrow(() -> new RsvpNotFoundException("RSVP not found for this event and student"));


        if (existingRsvp.getStatus() ==  RsvpStatus.CONFIRMED) {
            throw new RsvpAlreadyExistsException("RSVP is already confirmed for event: " + eventId);
        } else if (existingRsvp.getStatus() ==  RsvpStatus.CANCELLED) {
            existingRsvp.setStatus(RsvpStatus.CONFIRMED);
        }

        if (event.getType() == EventType.WITH_CAPACITY) {
            event.setAvailableCapacity(event.getAvailableCapacity() - 1);
            eventRepository.save(event);
        }

        return rsvpMapper.toDTO(rsvpRepository.save(existingRsvp));
    }
}