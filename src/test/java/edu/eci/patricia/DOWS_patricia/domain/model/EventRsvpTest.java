package edu.eci.patricia.DOWS_patricia.domain.model;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpTest {

    private EventRsvp rsvp;

    @BeforeEach
    void setUp() {
        rsvp = EventRsvp.builder()
            .id(new RsvpId("rsvp-001"))
            .eventId(new EventId("123e4567-e89b-12d3-a456-426614174000"))
            .studentId(new StudentId("student-001"))
            .confirmedAt(LocalDateTime.of(2026, 5, 1, 12, 0))
            .status(RsvpStatus.CONFIRMED)
            .build();
    }

    @Test
    void shouldCreateRsvpWithCorrectAttributes() {
        assertNotNull(rsvp);
        assertEquals("rsvp-001", rsvp.getId().getValue());
        assertEquals("123e4567-e89b-12d3-a456-426614174000", rsvp.getEventId().getValue());
        assertEquals("student-001", rsvp.getStudentId().getValue());
        assertEquals(RsvpStatus.CONFIRMED, rsvp.getStatus());
    }

    @Test
    void shouldUpdateRsvpStatus() {
        rsvp.setStatus(RsvpStatus.CANCELLED);
        assertEquals(RsvpStatus.CANCELLED, rsvp.getStatus());
    }

    @Test
    void shouldHaveCorrectConfirmedAt() {
        assertEquals(LocalDateTime.of(2026, 5, 1, 12, 0), rsvp.getConfirmedAt());
    }

    @Test
    void shouldCreateRsvpWithNoArgsConstructor() {
        EventRsvp emptyRsvp = new EventRsvp();
        assertNotNull(emptyRsvp);
        assertNull(emptyRsvp.getStatus());
        assertNull(emptyRsvp.getEventId());
    }

    @Test
    void shouldUpdateConfirmedAt() {
        LocalDateTime newDate = LocalDateTime.of(2026, 6, 1, 10, 0);
        rsvp.setConfirmedAt(newDate);
        assertEquals(newDate, rsvp.getConfirmedAt());
    }

    @Test
    void shouldHaveDifferentRsvpIds() {
        EventRsvp anotherRsvp = EventRsvp.builder()
            .id(new RsvpId("rsvp-002"))
            .eventId(new EventId("123e4567-e89b-12d3-a456-426614174000"))
            .studentId(new StudentId("student-002"))
            .confirmedAt(LocalDateTime.now())
            .status(RsvpStatus.CONFIRMED)
            .build();

        assertNotEquals(rsvp.getId().getValue(), anotherRsvp.getId().getValue());
    }
}
