package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotAvailableException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRsvpUseCase implements CreateRsvpPort {

    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRepositoryPort eventRepository;
    private final EventRsvpMapper rsvpMapper;

    @Override
    public EventResponseRsvp execute(EventRequestRsvp request) {
        Event event = eventRepository.findById(new EventId(request.getEventId()))
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new EventNotAvailableException("Event is cancelled");
        }
        if (event.getStatus() == EventStatus.FULL) {
            throw new EventNotAvailableException("Event is full");
        }

        event.setAvailableSpots(event.getAvailableSpots() - 1);

        if (event.getAvailableSpots() == 0) {
            event.setStatus(EventStatus.FULL);
        }

        eventRepository.save(event);

        EventRsvp rsvp = rsvpMapper.toDomain(request);
        return rsvpMapper.toResponse(rsvpRepository.save(rsvp));
    }
}