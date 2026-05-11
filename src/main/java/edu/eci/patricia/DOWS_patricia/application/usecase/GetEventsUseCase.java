package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.GetEventsPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetEventsUseCase implements GetEventsPort {



    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventResponse> execute(EventCategory categoryFilter, LocalDate dateFilter) {
        List<Event> events = eventRepository.findActiveWithFilters(categoryFilter, dateFilter);

        return events.stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

}