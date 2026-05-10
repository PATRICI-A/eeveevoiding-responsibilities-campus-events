package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpPersistenceMapperTest {

    private EventRsvpPersistenceMapper mapper;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        mapper = new EventRsvpPersistenceMapper();
        now = LocalDateTime.now();
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        EventRsvp rsvp = EventRsvp.builder()
                .id(new RsvpId("rsvp-id-123"))
                .eventId(new EventId("event-id-456"))
                .studentId(new StudentId("student-id-789"))
                .confirmedAt(now)
                .status(RsvpStatus.CONFIRMED)
                .build();

        EventRsvpEntity entity = mapper.toEntity(rsvp);

        assertNotNull(entity);
        assertEquals("rsvp-id-123", entity.getId());
        assertEquals("event-id-456", entity.getEventId());
        assertEquals("student-id-789", entity.getStudentId());
        assertEquals(now, entity.getConfirmedAt());
        assertEquals(RsvpStatus.CONFIRMED, entity.getStatus());
    }

    @Test
    void toDomain_ShouldMapAllFieldsCorrectly() {
        EventRsvpEntity entity = EventRsvpEntity.builder()
                .id("rsvp-id-123")
                .eventId("event-id-456")
                .studentId("student-id-789")
                .confirmedAt(now)
                .status(RsvpStatus.CONFIRMED)
                .build();

        EventRsvp rsvp = mapper.toDomain(entity);

        assertNotNull(rsvp);
        assertEquals("rsvp-id-123", rsvp.getId().getValue());
        assertEquals("event-id-456", rsvp.getEventId().getValue());
        assertEquals("student-id-789", rsvp.getStudentId().getValue());
        assertEquals(now, rsvp.getConfirmedAt());
        assertEquals(RsvpStatus.CONFIRMED, rsvp.getStatus());
    }

    @Test
    void toEntity_ThenToDomain_ShouldReturnEquivalentRsvp() {
        EventRsvp original = EventRsvp.builder()
                .id(new RsvpId("rsvp-id-123"))
                .eventId(new EventId("event-id-456"))
                .studentId(new StudentId("student-id-789"))
                .confirmedAt(now)
                .status(RsvpStatus.CONFIRMED)
                .build();

        EventRsvp result = mapper.toDomain(mapper.toEntity(original));

        assertEquals(original.getId().getValue(), result.getId().getValue());
        assertEquals(original.getEventId().getValue(), result.getEventId().getValue());
        assertEquals(original.getStudentId().getValue(), result.getStudentId().getValue());
        assertEquals(original.getConfirmedAt(), result.getConfirmedAt());
        assertEquals(original.getStatus(), result.getStatus());
    }
}
