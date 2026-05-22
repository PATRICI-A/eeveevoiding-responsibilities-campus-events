package edu.eci.patricia.domain.model;

import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpTest {

    private EventRsvp buildFullRsvp() {
        return EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(EventId.generate())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void shouldBuildEventRsvpWithAllFields() {
        EventRsvp rsvp = buildFullRsvp();
        assertNotNull(rsvp);
        assertNotNull(rsvp.getId());
        assertNotNull(rsvp.getEventId());
        assertNotNull(rsvp.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, rsvp.getStatus());
    }

    @Test
    void shouldSetAndGetId() {
        EventRsvp rsvp = buildFullRsvp();
        RsvpId newId = RsvpId.generate();
        rsvp.setId(newId);
        assertEquals(newId, rsvp.getId());
    }

    @Test
    void shouldSetAndGetEventId() {
        EventRsvp rsvp = buildFullRsvp();
        EventId newEventId = EventId.generate();
        rsvp.setEventId(newEventId);
        assertEquals(newEventId, rsvp.getEventId());
    }

    @Test
    void shouldSetAndGetStudentId() {
        EventRsvp rsvp = buildFullRsvp();
        UUID newStudentId = UUID.randomUUID();
        rsvp.setStudentId(newStudentId);
        assertEquals(newStudentId, rsvp.getStudentId());
    }

    @Test
    void shouldSetAndGetStatus() {
        EventRsvp rsvp = buildFullRsvp();
        rsvp.setStatus(RsvpStatus.CANCELLED);
        assertEquals(RsvpStatus.CANCELLED, rsvp.getStatus());
    }

    @Test
    void shouldSupportAllRsvpStatuses() {
        for (RsvpStatus status : RsvpStatus.values()) {
            EventRsvp rsvp = buildFullRsvp();
            rsvp.setStatus(status);
            assertEquals(status, rsvp.getStatus());
        }
    }

    @Test
    void shouldBuildEventRsvpWithNullOptionalFields() {
        EventRsvp rsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .build();
        assertNotNull(rsvp);
        assertNull(rsvp.getEventId());
        assertNull(rsvp.getStudentId());
        assertNull(rsvp.getStatus());
    }

    @Test
    void shouldCreateEventRsvpWithAllArgsConstructor() {
        RsvpId rsvpId = RsvpId.generate();
        EventId eventId = EventId.generate();
        UUID studentId = UUID.randomUUID();

        EventRsvp rsvp = new EventRsvp(rsvpId, eventId, studentId, RsvpStatus.CONFIRMED);

        assertEquals(rsvpId, rsvp.getId());
        assertEquals(eventId, rsvp.getEventId());
        assertEquals(studentId, rsvp.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, rsvp.getStatus());
    }

    @Test
    void shouldCreateCancelledRsvpWithAllArgsConstructor() {
        RsvpId rsvpId = RsvpId.generate();
        EventId eventId = EventId.generate();
        UUID studentId = UUID.randomUUID();

        EventRsvp rsvp = new EventRsvp(rsvpId, eventId, studentId, RsvpStatus.CANCELLED);

        assertEquals(RsvpStatus.CANCELLED, rsvp.getStatus());
    }
}
