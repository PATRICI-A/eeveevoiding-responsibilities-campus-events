package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventAlreadyExistsException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEventUseCase implements CreateEventPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponse execute(EventRequest request) {
        if (eventRepository.existsByName(request.getName())) {
            throw new EventAlreadyExistsException("Event with name '" + request.getName() + "' already exists");
        }

        Event event = eventMapper.toDomain(request);
        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
    }
}
