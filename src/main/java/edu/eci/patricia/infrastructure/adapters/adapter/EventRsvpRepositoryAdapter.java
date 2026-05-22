package edu.eci.patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.infrastructure.adapters.persistence.mapper.EventRsvpPersistenceMapper;
import edu.eci.patricia.infrastructure.adapters.persistence.repository.EventRsvpMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventRsvpRepositoryAdapter implements EventRsvpRepositoryPort {

    private final EventRsvpMongoRepository repository;
    private final EventRsvpPersistenceMapper mapper;

    @Override
    public EventRsvp save(EventRsvp rsvp) {
        return mapper.toModel(repository.save(mapper.toEntity(rsvp)));
    }

    @Override
    public boolean existsByEventIdAndStudentId(EventId eventId, UUID studentId) {
        return repository.existsByEventIdAndStudentId(eventId.getValue(), studentId);
    }

    @Override
    public List<EventRsvp> findConfirmedByStudentId(UUID studentId) {
        return repository.findByStudentIdAndStatus(studentId, RsvpStatus.CONFIRMED)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public List<EventRsvp> findConfirmedByEventId(EventId eventId) {
        return repository.findByEventIdAndStatus(eventId.getValue(), RsvpStatus.CONFIRMED)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<EventRsvp> findByEventIdAndStudentId(UUID eventId, UUID studentId) {
        return repository.findByEventIdAndStudentId(eventId, studentId)
                .map(mapper::toModel);
    }

    @Override
    public List<EventRsvp> findByStudentId(UUID studentId) {
        return repository.findByStudentId(studentId)
                .stream().map(mapper::toModel).toList();
    }
}