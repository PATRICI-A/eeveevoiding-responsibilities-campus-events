package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper.EventPersistenceMapper;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.repository.EventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepositoryPort {

    private final EventMongoRepository mongoRepository;
    private final EventPersistenceMapper mapper;

    @Override
    public Event save(Event event) {
        return mapper.toModel(mongoRepository.save(mapper.toEntity(event)));
    }

    @Override
    public Optional<Event> findById(EventId id) {
        return mongoRepository.findById(id.getValue())
                .map(mapper::toModel);
    }

    @Override
    public List<Event> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return mongoRepository.existsByName(name);
    }
}