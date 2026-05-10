package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventAlreadyCancelledException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelEventUseCase implements CancelEventPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponse execute(String id) {
        Event event = eventRepository.findById(new EventId(id))
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new EventAlreadyCancelledException("Event is already cancelled");
        }

        event.setStatus(EventStatus.CANCELLED);
        Event saved = eventRepository.save(event);
        return eventMapper.toDTO(saved);
    }
}