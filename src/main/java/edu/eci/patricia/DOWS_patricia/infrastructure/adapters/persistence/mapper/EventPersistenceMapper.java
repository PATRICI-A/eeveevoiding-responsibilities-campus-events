package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.OrganizerId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class EventPersistenceMapper {

    public EventEntity toEntity(Event event) {
        return EventEntity.builder()
                .id(event.getId().getValue())
                .name(event.getName())
                .description(event.getDescription())
                .dateTime(event.getDateTime())
                .location(event.getLocation())
                .category(event.getCategory())
                .type(event.getType())
                .maxCapacity(event.getMaxCapacity())
                .availableSpots(event.getAvailableSpots())
                .organizerId(event.getOrganizerId().getValue())
                .status(event.getStatus())
                .createdAt(event.getCreatedAt())
                .build();
    }

    public Event toDomain(EventEntity entity) {
        return Event.builder()
                .id(new EventId(entity.getId()))
                .name(entity.getName())
                .description(entity.getDescription())
                .dateTime(entity.getDateTime())
                .location(entity.getLocation())
                .category(entity.getCategory())
                .type(entity.getType())
                .maxCapacity(entity.getMaxCapacity())
                .availableSpots(entity.getAvailableSpots())
                .organizerId(new OrganizerId(entity.getOrganizerId()))
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}