package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.ports.in.GetEventsPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetEventsUseCase implements GetEventsPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventResponse> execute(EventCategory category, LocalDate date) {
        return eventRepository.findActiveEvents(category, date)
                .stream()
                .map(eventMapper::toDTO)
                .toList();
    }
}