package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper.EventPersistenceMapper;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.repository.EventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepositoryPort {

    private final EventMongoRepository repository;
    private final EventPersistenceMapper mapper;

    @Override
    public Event save(Event event) {
        return mapper.toModel(repository.save(mapper.toEntity(event)));
    }

    @Override
    public Optional<Event> findById(EventId id) {
        return repository.findById(id.getValue())
                .map(mapper::toModel);
    }

    @Override
    public List<Event> findActiveEvents(EventCategory category, LocalDate date) {
        EventStatus active = EventStatus.ACTIVE;

        if (category != null && date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);
            return repository.findByStatusAndCategoryAndDateTimeBetween(active, category, start, end)
                    .stream().map(mapper::toModel).toList();
        }
        if (category != null) {
            return repository.findByStatusAndCategory(active, category)
                    .stream().map(mapper::toModel).toList();
        }
        if (date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);
            return repository.findByStatusAndDateTimeBetween(active, start, end)
                    .stream().map(mapper::toModel).toList();
        }
        return repository.findByStatus(active)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Event> findByStatus(EventStatus status) {
        return repository.findByStatus(status)
                .stream().map(mapper::toModel).toList();
    }
}