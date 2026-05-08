package edu.eci.patricia.DOWS_patricia.domain.model;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.OrganizerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;

    @BeforeEach
    void setUp() {
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
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void shouldCreateEventWithCorrectAttributes() {
        assertNotNull(event);
        assertEquals("Tech Talk", event.getName());
        assertEquals("A tech talk event", event.getDescription());
        assertEquals("ECI Auditorium", event.getLocation());
        assertEquals(EventCategory.ACADEMIC, event.getCategory());
        assertEquals(EventType.OPEN , event.getType());
        assertEquals(100, event.getMaxCapacity());
        assertEquals(100, event.getAvailableSpots());
        assertEquals(EventStatus.ACTIVE, event.getStatus());
    }

    @Test
    void shouldUpdateEventStatus() {
        event.setStatus(EventStatus.CANCELLED);
        assertEquals(EventStatus.CANCELLED, event.getStatus());
    }

    @Test
    void shouldUpdateAvailableSpotsWhenReserved() {
        event.setAvailableSpots(event.getAvailableSpots() - 1);
        assertEquals(99, event.getAvailableSpots());
    }

    @Test
    void shouldSetStatusToFullWhenNoSpotsAvailable() {
        event.setAvailableSpots(0);
        event.setStatus(EventStatus.FULL);
        assertEquals(0, event.getAvailableSpots());
        assertEquals(EventStatus.FULL, event.getStatus());
    }

    @Test
    void shouldRestoreAvailableSpotWhenRsvpCancelled() {
        event.setAvailableSpots(0);
        event.setStatus(EventStatus.FULL);
        event.setAvailableSpots(event.getAvailableSpots() + 1);
        event.setStatus(EventStatus.ACTIVE);
        assertEquals(1, event.getAvailableSpots());
        assertEquals(EventStatus.ACTIVE, event.getStatus());
    }

    @Test
    void shouldHaveCorrectEventId() {
        assertEquals("123e4567-e89b-12d3-a456-426614174000", event.getId().getValue());
    }

    @Test
    void shouldHaveCorrectOrganizerId() {
        assertEquals("org-001", event.getOrganizerId().getValue());
    }

    @Test
    void shouldCreateEventWithNoArgsConstructor() {
        Event emptyEvent = new Event();
        assertNotNull(emptyEvent);
        assertNull(emptyEvent.getName());
        assertNull(emptyEvent.getStatus());
    }

    @Test
    void shouldUpdateEventName() {
        event.setName("Updated Tech Talk");
        assertEquals("Updated Tech Talk", event.getName());
    }
}
