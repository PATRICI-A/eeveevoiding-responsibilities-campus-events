package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.GetEventsPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetEventsUseCase implements GetEventsPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventResponse> execute() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }
}