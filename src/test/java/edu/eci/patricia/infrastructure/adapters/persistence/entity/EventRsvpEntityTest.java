package edu.eci.patricia.infrastructure.adapters.persistence.entity;

import edu.eci.patricia.domain.model.enums.RsvpStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpEntityTest {

    private EventRsvpEntity buildFullEntity() {
        return EventRsvpEntity.builder()
                .id(UUID.randomUUID())
                .eventId(UUID.randomUUID())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CONFIRMED)
                .confirmedAt(LocalDateTime.of(2025, 6, 15, 10, 0))
                .cancelledAt(null)
                .build();
    }

    @Test
    void shouldBuildEventRsvpEntityWithAllFields() {
        EventRsvpEntity entity = buildFullEntity();
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertNotNull(entity.getEventId());
        assertNotNull(entity.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, entity.getStatus());
        assertNotNull(entity.getConfirmedAt());
        assertNull(entity.getCancelledAt());
    }

    @Test
    void shouldCreateEventRsvpEntityWithNoArgsConstructor() {
        EventRsvpEntity entity = new EventRsvpEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getEventId());
        assertNull(entity.getStudentId());
        assertNull(entity.getStatus());
        assertNull(entity.getConfirmedAt());
        assertNull(entity.getCancelledAt());
    }

    @Test
    void shouldCreateEventRsvpEntityWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime confirmedAt = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime cancelledAt = LocalDateTime.of(2025, 6, 16, 12, 0);

        EventRsvpEntity entity = new EventRsvpEntity(id, eventId, studentId,
                RsvpStatus.CANCELLED, confirmedAt, cancelledAt);

        assertEquals(id, entity.getId());
        assertEquals(eventId, entity.getEventId());
        assertEquals(studentId, entity.getStudentId());
        assertEquals(RsvpStatus.CANCELLED, entity.getStatus());
        assertEquals(confirmedAt, entity.getConfirmedAt());
        assertEquals(cancelledAt, entity.getCancelledAt());
    }

    @Test
    void shouldSetAndGetId() {
        EventRsvpEntity entity = buildFullEntity();
        UUID newId = UUID.randomUUID();
        entity.setId(newId);
        assertEquals(newId, entity.getId());
    }

    @Test
    void shouldSetAndGetEventId() {
        EventRsvpEntity entity = buildFullEntity();
        UUID newEventId = UUID.randomUUID();
        entity.setEventId(newEventId);
        assertEquals(newEventId, entity.getEventId());
    }

    @Test
    void shouldSetAndGetStudentId() {
        EventRsvpEntity entity = buildFullEntity();
        UUID newStudentId = UUID.randomUUID();
        entity.setStudentId(newStudentId);
        assertEquals(newStudentId, entity.getStudentId());
    }

    @Test
    void shouldSetAndGetStatusConfirmed() {
        EventRsvpEntity entity = buildFullEntity();
        entity.setStatus(RsvpStatus.CONFIRMED);
        assertEquals(RsvpStatus.CONFIRMED, entity.getStatus());
    }

    @Test
    void shouldSetAndGetStatusCancelled() {
        EventRsvpEntity entity = buildFullEntity();
        entity.setStatus(RsvpStatus.CANCELLED);
        assertEquals(RsvpStatus.CANCELLED, entity.getStatus());
    }

    @Test
    void shouldSetAndGetConfirmedAt() {
        EventRsvpEntity entity = buildFullEntity();
        LocalDateTime newTime = LocalDateTime.of(2025, 9, 1, 8, 30);
        entity.setConfirmedAt(newTime);
        assertEquals(newTime, entity.getConfirmedAt());
    }

    @Test
    void shouldSetAndGetCancelledAt() {
        EventRsvpEntity entity = buildFullEntity();
        LocalDateTime cancelTime = LocalDateTime.of(2025, 9, 5, 15, 0);
        entity.setCancelledAt(cancelTime);
        assertEquals(cancelTime, entity.getCancelledAt());
    }

    @Test
    void shouldSetCancelledAtToNull() {
        EventRsvpEntity entity = buildFullEntity();
        entity.setCancelledAt(null);
        assertNull(entity.getCancelledAt());
    }

    @Test
    void shouldSetConfirmedAtToNull() {
        EventRsvpEntity entity = buildFullEntity();
        entity.setConfirmedAt(null);
        assertNull(entity.getConfirmedAt());
    }

    @Test
    void shouldBeEqualWhenSameFields() {
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime confirmedAt = LocalDateTime.of(2025, 6, 15, 10, 0);

        EventRsvpEntity a = EventRsvpEntity.builder().id(id).eventId(eventId)
                .studentId(studentId).status(RsvpStatus.CONFIRMED)
                .confirmedAt(confirmedAt).build();

        EventRsvpEntity b = EventRsvpEntity.builder().id(id).eventId(eventId)
                .studentId(studentId).status(RsvpStatus.CONFIRMED)
                .confirmedAt(confirmedAt).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        EventRsvpEntity a = buildFullEntity();
        EventRsvpEntity b = buildFullEntity();
        assertNotEquals(a, b);
    }

    @Test
    void shouldReturnToStringWithClassName() {
        EventRsvpEntity entity = buildFullEntity();
        String toString = entity.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("EventRsvpEntity"));
    }

    @Test
    void shouldSupportAllRsvpStatuses() {
        for (RsvpStatus status : RsvpStatus.values()) {
            EventRsvpEntity entity = buildFullEntity();
            entity.setStatus(status);
            assertEquals(status, entity.getStatus());
        }
    }

    @Test
    void shouldBuildWithNullOptionalFields() {
        EventRsvpEntity entity = EventRsvpEntity.builder()
                .id(UUID.randomUUID())
                .build();
        assertNotNull(entity);
        assertNull(entity.getEventId());
        assertNull(entity.getStudentId());
        assertNull(entity.getStatus());
        assertNull(entity.getConfirmedAt());
        assertNull(entity.getCancelledAt());
    }

    @Test
    void shouldBuildCancelledRsvpWithBothTimestamps() {
        LocalDateTime confirmedAt = LocalDateTime.of(2025, 5, 1, 9, 0);
        LocalDateTime cancelledAt = LocalDateTime.of(2025, 5, 2, 11, 0);

        EventRsvpEntity entity = EventRsvpEntity.builder()
                .id(UUID.randomUUID())
                .eventId(UUID.randomUUID())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CANCELLED)
                .confirmedAt(confirmedAt)
                .cancelledAt(cancelledAt)
                .build();

        assertEquals(RsvpStatus.CANCELLED, entity.getStatus());
        assertEquals(confirmedAt, entity.getConfirmedAt());
        assertEquals(cancelledAt, entity.getCancelledAt());
    }
}
