package edu.eci.patricia.DOWS_patricia.application.mapper;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    public Event toDomain(EventRequest request) {
        return Event.builder()
                .id(EventId.generate())
                .name(request.getName())
                .description(request.getDescription())
                .dateTime(request.getDateTime())
                .location(request.getLocation())
                .category(request.getCategory())
                .type(request.getType())
                .maxCapacity(request.getMaxCapacity())
                .availableSpots(request.getMaxCapacity())
                .organizerId(new OrganizerId(request.getOrganizerId()))
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public EventResponse toResponse(Event event) {
        return EventResponse.builder()
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
}