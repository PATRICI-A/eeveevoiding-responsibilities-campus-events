package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper.EventRsvpPersistenceMapper;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.repository.EventRsvpMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventRsvpRepositoryAdapter implements EventRsvpRepositoryPort {

    private final EventRsvpMongoRepository mongoRepository;
    private final EventRsvpPersistenceMapper mapper;

    @Override
    public EventRsvp save(EventRsvp rsvp) {
        return mapper.toDomain(mongoRepository.save(mapper.toEntity(rsvp)));
    }

    @Override
    public Optional<EventRsvp> findById(RsvpId id) {
        return mongoRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }
}