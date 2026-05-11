package edu.eci.patricia.DOWS_patricia.domain.model;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpTest {

    private EventRsvp buildDefaultRsvp() {
        return EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(EventId.generate())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CONFIRMED)
                .confirmedAt(LocalDateTime.of(2025, 6, 10, 9, 0))
                .cancelledAt(null)
                .build();
    }

    @Test
    void shouldBuildEventRsvpWithAllFields() {
        EventRsvp rsvp = buildDefaultRsvp();
        assertNotNull(rsvp);
        assertNotNull(rsvp.getId());
        assertNotNull(rsvp.getEventId());
        assertNotNull(rsvp.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, rsvp.getStatus());
        assertNotNull(rsvp.getConfirmedAt());
        assertNull(rsvp.getCancelledAt());
    }

    @Test
    void shouldAllowSettingStatus() {
        EventRsvp rsvp = buildDefaultRsvp();
        rsvp.setStatus(RsvpStatus.CANCELLED);
        assertEquals(RsvpStatus.CANCELLED, rsvp.getStatus());
    }

    @Test
    void shouldAllowSettingCancelledAt() {
        EventRsvp rsvp = buildDefaultRsvp();
        LocalDateTime cancelledAt = LocalDateTime.of(2025, 6, 12, 15, 30);
        rsvp.setCancelledAt(cancelledAt);
        assertEquals(cancelledAt, rsvp.getCancelledAt());
    }

    @Test
    void shouldAllowSettingConfirmedAt() {
        EventRsvp rsvp = buildDefaultRsvp();
        LocalDateTime confirmedAt = LocalDateTime.of(2025, 6, 11, 8, 0);
        rsvp.setConfirmedAt(confirmedAt);
        assertEquals(confirmedAt, rsvp.getConfirmedAt());
    }

    @Test
    void shouldAllowSettingStudentId() {
        EventRsvp rsvp = buildDefaultRsvp();
        UUID newStudent = UUID.randomUUID();
        rsvp.setStudentId(newStudent);
        assertEquals(newStudent, rsvp.getStudentId());
    }

    @Test
    void shouldAllowSettingEventId() {
        EventRsvp rsvp = buildDefaultRsvp();
        EventId newEventId = EventId.generate();
        rsvp.setEventId(newEventId);
        assertEquals(newEventId, rsvp.getEventId());
    }

    @Test
    void shouldAllowSettingId() {
        EventRsvp rsvp = buildDefaultRsvp();
        RsvpId newId = RsvpId.generate();
        rsvp.setId(newId);
        assertEquals(newId, rsvp.getId());
    }

    @Test
    void shouldBuildCancelledRsvpWithCancelledAt() {
        LocalDateTime cancelledAt = LocalDateTime.of(2025, 6, 13, 10, 0);
        EventRsvp rsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(EventId.generate())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CANCELLED)
                .confirmedAt(null)
                .cancelledAt(cancelledAt)
                .build();

        assertEquals(RsvpStatus.CANCELLED, rsvp.getStatus());
        assertEquals(cancelledAt, rsvp.getCancelledAt());
        assertNull(rsvp.getConfirmedAt());
    }

    @Test
    void shouldSupportAllRsvpStatuses() {
        for (RsvpStatus status : RsvpStatus.values()) {
            EventRsvp rsvp = EventRsvp.builder()
                    .id(RsvpId.generate())
                    .eventId(EventId.generate())
                    .studentId(UUID.randomUUID())
                    .status(status)
                    .build();
            assertEquals(status, rsvp.getStatus());
        }
    }
}
