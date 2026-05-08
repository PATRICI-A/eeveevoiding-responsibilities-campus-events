package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.RsvpNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelRsvpUseCase implements CancelRsvpPort {

    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRepositoryPort eventRepository;
    private final EventRsvpMapper rsvpMapper;

    @Override
    public EventResponseRsvp execute(String id) {
        EventRsvp rsvp = rsvpRepository.findById(new RsvpId(id))
                .orElseThrow(() -> new RsvpNotFoundException("RSVP not found with id: " + id));

        Event event = eventRepository.findById(rsvp.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        event.setAvailableSpots(event.getAvailableSpots() + 1);

        if (event.getStatus() == EventStatus.FULL) {
            event.setStatus(EventStatus.ACTIVE);
        }

        eventRepository.save(event);

        rsvp.setStatus(RsvpStatus.CANCELLED);
        return rsvpMapper.toResponse(rsvpRepository.save(rsvp));
    }
}