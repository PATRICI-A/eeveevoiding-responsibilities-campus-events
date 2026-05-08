package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpEntityTest {

    private EventRsvpEntity rsvpEntity;

    @BeforeEach
    void setUp() {
        rsvpEntity = EventRsvpEntity.builder()
                .id("rsvp-001")
                .eventId("123e4567-e89b-12d3-a456-426614174000")
                .studentId("student-001")
                .confirmedAt(LocalDateTime.of(2026, 5, 1, 12, 0))
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void shouldCreateEventRsvpEntityWithCorrectAttributes() {
        assertNotNull(rsvpEntity);
        assertEquals("rsvp-001", rsvpEntity.getId());
        assertEquals("123e4567-e89b-12d3-a456-426614174000", rsvpEntity.getEventId());
        assertEquals("student-001", rsvpEntity.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, rsvpEntity.getStatus());
        assertEquals(LocalDateTime.of(2026, 5, 1, 12, 0), rsvpEntity.getConfirmedAt());
    }

    @Test
    void shouldCreateEventRsvpEntityWithNoArgsConstructor() {
        EventRsvpEntity empty = new EventRsvpEntity();
        assertNotNull(empty);
        assertNull(empty.getId());
        assertNull(empty.getStatus());
    }

    @Test
    void shouldUpdateRsvpStatus() {
        rsvpEntity.setStatus(RsvpStatus.CANCELLED);
        assertEquals(RsvpStatus.CANCELLED, rsvpEntity.getStatus());
    }
}
