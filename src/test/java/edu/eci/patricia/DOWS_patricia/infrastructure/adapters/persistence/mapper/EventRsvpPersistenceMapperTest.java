package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class   EventRsvpPersistenceMapperTest {

    private EventRsvpPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EventRsvpPersistenceMapper.class);
    }

    @Test
    void toEntity_shouldMapAllFields() {
        UUID rsvpUUID = UUID.randomUUID();
        UUID eventUUID = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        EventRsvp eventRsvp = EventRsvp.builder()
                .id(new RsvpId(rsvpUUID))
                .eventId(new EventId(eventUUID))
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .confirmedAt(now)
                .cancelledAt(null)
                .build();

        EventRsvpEntity entity = mapper.toEntity(eventRsvp);

        assertNotNull(entity);
        assertEquals(rsvpUUID, entity.getId());
        assertEquals(eventUUID, entity.getEventId());
        assertEquals(studentId, entity.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, entity.getStatus());
        assertEquals(now, entity.getConfirmedAt());
        assertNull(entity.getCancelledAt());
    }

    @Test
    void toModel_shouldMapAllFields() {
        UUID rsvpUUID = UUID.randomUUID();
        UUID eventUUID = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        EventRsvpEntity entity = EventRsvpEntity.builder()
                .id(rsvpUUID)
                .eventId(eventUUID)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .confirmedAt(now)
                .cancelledAt(null)
                .build();

        EventRsvp model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(rsvpUUID, model.getId().getValue());
        assertEquals(eventUUID, model.getEventId().getValue());
        assertEquals(studentId, model.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, model.getStatus());
        assertEquals(now, model.getConfirmedAt());
        assertNull(model.getCancelledAt());
    }

    @Test
    void toModel_shouldMapCancelledStatus() {
        LocalDateTime now = LocalDateTime.now();

        EventRsvpEntity entity = EventRsvpEntity.builder()
                .id(UUID.randomUUID())
                .eventId(UUID.randomUUID())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CANCELLED)
                .confirmedAt(null)
                .cancelledAt(now)
                .build();

        EventRsvp model = mapper.toModel(entity);

        assertEquals(RsvpStatus.CANCELLED, model.getStatus());
        assertNull(model.getConfirmedAt());
        assertEquals(now, model.getCancelledAt());
    }

    @Test
    void rsvpIdToUUID_shouldReturnUUID_whenRsvpIdIsNotNull() {
        UUID uuid = UUID.randomUUID();
        RsvpId rsvpId = new RsvpId(uuid);

        UUID result = mapper.rsvpIdToUUID(rsvpId);

        assertEquals(uuid, result);
    }

    @Test
    void rsvpIdToUUID_shouldReturnNull_whenRsvpIdIsNull() {
        UUID result = mapper.rsvpIdToUUID(null);

        assertNull(result);
    }

    @Test
    void uuidToRsvpId_shouldReturnRsvpId_whenUUIDIsNotNull() {
        UUID uuid = UUID.randomUUID();

        RsvpId result = mapper.uuidToRsvpId(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getValue());
    }

    @Test
    void uuidToRsvpId_shouldReturnNull_whenUUIDIsNull() {
        RsvpId result = mapper.uuidToRsvpId(null);

        assertNull(result);
    }

    @Test
    void eventIdToUUID_shouldReturnUUID_whenEventIdIsNotNull() {
        UUID uuid = UUID.randomUUID();
        EventId eventId = new EventId(uuid);

        UUID result = mapper.eventIdToUUID(eventId);

        assertEquals(uuid, result);
    }

    @Test
    void eventIdToUUID_shouldReturnNull_whenEventIdIsNull() {
        UUID result = mapper.eventIdToUUID(null);

        assertNull(result);
    }

    @Test
    void uuidToEventId_shouldReturnEventId_whenUUIDIsNotNull() {
        UUID uuid = UUID.randomUUID();

        EventId result = mapper.uuidToEventId(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getValue());
    }

    @Test
    void uuidToEventId_shouldReturnNull_whenUUIDIsNull() {
        EventId result = mapper.uuidToEventId(null);

        assertNull(result);
    }
}
