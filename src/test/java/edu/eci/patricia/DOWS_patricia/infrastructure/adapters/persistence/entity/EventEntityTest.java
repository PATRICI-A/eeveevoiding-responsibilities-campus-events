package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventEntityTest {

    private EventEntity eventEntity;

    @BeforeEach
    void setUp() {
        eventEntity = EventEntity.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .name("Tech Talk")
                .description("A tech talk event")
                .dateTime(LocalDateTime.of(2026, 6, 15, 10, 0))
                .location("ECI Auditorium")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(100)
                .organizerId("org-001")
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2026, 5, 1, 12, 0))
                .build();
    }

    @Test
    void shouldCreateEventEntityWithCorrectAttributes() {
        assertNotNull(eventEntity);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", eventEntity.getId());
        assertEquals("Tech Talk", eventEntity.getName());
        assertEquals("A tech talk event", eventEntity.getDescription());
        assertEquals("ECI Auditorium", eventEntity.getLocation());
        assertEquals(EventCategory.ACADEMIC, eventEntity.getCategory());
        assertEquals(EventType.OPEN, eventEntity.getType());
        assertEquals(100, eventEntity.getMaxCapacity());
        assertEquals(100, eventEntity.getAvailableSpots());
        assertEquals("org-001", eventEntity.getOrganizerId());
        assertEquals(EventStatus.ACTIVE, eventEntity.getStatus());
    }

    @Test
    void shouldCreateEventEntityWithNoArgsConstructor() {
        EventEntity empty = new EventEntity();
        assertNotNull(empty);
        assertNull(empty.getId());
        assertNull(empty.getName());
    }

    @Test
    void shouldUpdateEventEntityStatus() {
        eventEntity.setStatus(EventStatus.CANCELLED);
        assertEquals(EventStatus.CANCELLED, eventEntity.getStatus());
    }

    @Test
    void shouldUpdateAvailableSpots() {
        eventEntity.setAvailableSpots(50);
        assertEquals(50, eventEntity.getAvailableSpots());
    }
}
