package edu.eci.patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpPersistenceMapperTest {

    private EventRsvpPersistenceMapper eventRsvpPersistenceMapper;

    @BeforeEach
    void setUp() {
        eventRsvpPersistenceMapper = Mappers.getMapper(EventRsvpPersistenceMapper.class);
    }

    // ── toEntity ──────────────────────────────────────────────

    @Test
    void toEntity_shouldMapDomainToEntity_whenConfirmedRsvp() {
        UUID rsvpUUID = UUID.randomUUID();
        UUID eventUUID = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        EventRsvp rsvp = EventRsvp.builder()
                .id(new RsvpId(rsvpUUID))
                .eventId(new EventId(eventUUID))
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        EventRsvpEntity result = eventRsvpPersistenceMapper.toEntity(rsvp);

        assertNotNull(result);
        assertEquals(rsvpUUID, result.getId());
        assertEquals(eventUUID, result.getEventId());
        assertEquals(studentId, result.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void toEntity_shouldMapDomainToEntity_whenCancelledRsvp() {
        EventRsvp rsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(EventId.generate())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CANCELLED)
                .build();

        EventRsvpEntity result = eventRsvpPersistenceMapper.toEntity(rsvp);

        assertEquals(RsvpStatus.CANCELLED, result.getStatus());
    }

    // ── toModel ───────────────────────────────────────────────

    @Test
    void toModel_shouldMapEntityToDomain_whenValidEntity() {
        UUID rsvpUUID = UUID.randomUUID();
        UUID eventUUID = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        EventRsvpEntity entity = EventRsvpEntity.builder()
                .id(rsvpUUID)
                .eventId(eventUUID)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        EventRsvp result = eventRsvpPersistenceMapper.toModel(entity);

        assertNotNull(result);
        assertEquals(rsvpUUID, result.getId().getValue());
        assertEquals(eventUUID, result.getEventId().getValue());
        assertEquals(studentId, result.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void toModel_shouldMapCancelledStatus_whenCancelledEntity() {
        EventRsvpEntity entity = EventRsvpEntity.builder()
                .id(UUID.randomUUID())
                .eventId(UUID.randomUUID())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CANCELLED)
                .build();

        EventRsvp result = eventRsvpPersistenceMapper.toModel(entity);

        assertEquals(RsvpStatus.CANCELLED, result.getStatus());
    }

    // ── rsvpIdToUUID / uuidToRsvpId ───────────────────────────

    @Test
    void rsvpIdToUUID_shouldReturnUUID_whenRsvpIdNotNull() {
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, eventRsvpPersistenceMapper.rsvpIdToUUID(new RsvpId(uuid)));
    }

    @Test
    void rsvpIdToUUID_shouldReturnNull_whenRsvpIdIsNull() {
        assertNull(eventRsvpPersistenceMapper.rsvpIdToUUID(null));
    }

    @Test
    void uuidToRsvpId_shouldReturnRsvpId_whenUUIDNotNull() {
        UUID uuid = UUID.randomUUID();
        RsvpId result = eventRsvpPersistenceMapper.uuidToRsvpId(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getValue());
    }

    @Test
    void uuidToRsvpId_shouldReturnNull_whenUUIDIsNull() {
        assertNull(eventRsvpPersistenceMapper.uuidToRsvpId(null));
    }

    // ── eventIdToUUID / uuidToEventId ─────────────────────────

    @Test
    void eventIdToUUID_shouldReturnUUID_whenEventIdNotNull() {
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, eventRsvpPersistenceMapper.eventIdToUUID(new EventId(uuid)));
    }

    @Test
    void eventIdToUUID_shouldReturnNull_whenEventIdIsNull() {
        assertNull(eventRsvpPersistenceMapper.eventIdToUUID(null));
    }

    @Test
    void uuidToEventId_shouldReturnEventId_whenUUIDNotNull() {
        UUID uuid = UUID.randomUUID();
        EventId result = eventRsvpPersistenceMapper.uuidToEventId(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getValue());
    }

    @Test
    void uuidToEventId_shouldReturnNull_whenUUIDIsNull() {
        assertNull(eventRsvpPersistenceMapper.uuidToEventId(null));
    }
}
