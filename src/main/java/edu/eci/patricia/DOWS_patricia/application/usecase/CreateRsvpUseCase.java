package edu.eci.patricia.DOWS_patricia.application.usecase;


import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;

import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRsvpRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRsvpUseCase implements CreateRsvpPort {

    private final EventRepositoryPort eventRepository;
    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRsvpMapper rsvpMapper;

    @Override
    public EventResponseRsvp execute(EventRequestRsvp request) {


        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        event.validateCanReceiveRsvp();


        EventRsvp eventRsvp =  rsvpRepository.findByUserAndEvent(request.getStudentId(), request.getEventId());


        EventRsvp rsvp;
        if (eventRsvp.isPresent()) {
            rsvp = eventRsvp.get();
            rsvp.reactivate();
        } else {
            rsvp = new EventRsvp(eventId, studentId);
            rsvp.confirm();
        }

        event.decreaseCapacity();

        rsvpRepository.save(rsvp);
        eventRepository.save(event);

        return rsvpMapper.toDTO(rsvp);
    }
}