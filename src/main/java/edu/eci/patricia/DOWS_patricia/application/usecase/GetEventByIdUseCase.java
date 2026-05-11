package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.GetEventByIdPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetEventByIdUseCase implements GetEventByIdPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponse execute(UUID eventId) {
        return eventRepository.findById(new EventId(eventId))
                .map(eventMapper::toDTO)
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));
    }
}