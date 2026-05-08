package edu.eci.patricia.DOWS_patricia.application.mapper;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.OrganizerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    private EventMapper eventMapper;
    private EventRequest request;
    private Event event;

    @BeforeEach
    void setUp() {
        eventMapper = new EventMapper();

        request = EventRequest.builder()
                .name("Tech Talk")
                .description("A tech talk event")
                .dateTime(LocalDateTime.of(2026, 6, 15, 10, 0))
                .location("ECI Auditorium")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .organizerId("org-001")
                .build();

        event = Event.builder()
                .id(new EventId("123e4567-e89b-12d3-a456-426614174000"))
                .name("Tech Talk")
                .description("A tech talk event")
                .dateTime(LocalDateTime.of(2026, 6, 15, 10, 0))
                .location("ECI Auditorium")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(100)
                .organizerId(new OrganizerId("org-001"))
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2026, 5, 1, 12, 0))
                .build();
    }

    @Test
    void shouldMapRequestToDomain() {
        Event result = eventMapper.toDomain(request);
        assertNotNull(result);
        assertEquals("Tech Talk", result.getName());
        assertEquals("A tech talk event", result.getDescription());
        assertEquals("ECI Auditorium", result.getLocation());
        assertEquals(EventCategory.ACADEMIC, result.getCategory());
        assertEquals(EventType.OPEN, result.getType());
        assertEquals(100, result.getMaxCapacity());
        assertEquals(100, result.getAvailableSpots());
        assertEquals(EventStatus.ACTIVE, result.getStatus());
        assertEquals("org-001", result.getOrganizerId().getValue());
    }

    @Test
    void shouldGenerateEventIdWhenMappingToDomain() {
        Event result = eventMapper.toDomain(request);
        assertNotNull(result.getId());
        assertNotNull(result.getId().getValue());
        assertFalse(result.getId().getValue().isBlank());
    }

    @Test
    void shouldSetAvailableSpotsEqualToMaxCapacityWhenMappingToDomain() {
        Event result = eventMapper.toDomain(request);
        assertEquals(result.getMaxCapacity(), result.getAvailableSpots());
    }

    @Test
    void shouldSetCreatedAtWhenMappingToDomain() {
        Event result = eventMapper.toDomain(request);
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void shouldMapDomainToResponse() {
        EventResponse result = eventMapper.toResponse(event);
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
        assertEquals("Tech Talk", result.getName());
        assertEquals("A tech talk event", result.getDescription());
        assertEquals("ECI Auditorium", result.getLocation());
        assertEquals(EventCategory.ACADEMIC, result.getCategory());
        assertEquals(EventType.OPEN, result.getType());
        assertEquals(100, result.getMaxCapacity());
        assertEquals(100, result.getAvailableSpots());
        assertEquals("org-001", result.getOrganizerId());
        assertEquals(EventStatus.ACTIVE, result.getStatus());
    }

    @Test
    void shouldMapOrganizerIdAsStringInResponse() {
        EventResponse result = eventMapper.toResponse(event);
        assertEquals("org-001", result.getOrganizerId());
    }

    @Test
    void shouldMapEventIdAsStringInResponse() {
        EventResponse result = eventMapper.toResponse(event);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getId());
    }
}
