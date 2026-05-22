package edu.eci.patricia.application.mapper;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpMapperTest {

    private EventRsvpMapper eventRsvpMapper;

    @BeforeEach
    void setUp() {
        eventRsvpMapper = Mappers.getMapper(EventRsvpMapper.class);
    }

    // ── toDTO ─────────────────────────────────────────────────

    @Test
    void toDTO_shouldMapRsvpToResponse_whenConfirmed() {
        UUID rsvpUUID = UUID.randomUUID();
        UUID eventUUID = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        EventRsvp rsvp = EventRsvp.builder()
                .id(new RsvpId(rsvpUUID))
                .eventId(new EventId(eventUUID))
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        EventResponseRsvp result = eventRsvpMapper.toDTO(rsvp);

        assertNotNull(result);
        assertEquals(rsvpUUID, result.getId());
        assertEquals(eventUUID, result.getEventId());
        assertEquals(studentId, result.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void toDTO_shouldMapRsvpToResponse_whenCancelled() {
        EventRsvp rsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(EventId.generate())
                .studentId(UUID.randomUUID())
                .status(RsvpStatus.CANCELLED)
                .build();

        EventResponseRsvp result = eventRsvpMapper.toDTO(rsvp);

        assertEquals(RsvpStatus.CANCELLED, result.getStatus());
    }

    @Test
    void toDTO_shouldReturnNull_whenRsvpIsNull() {
        assertNull(eventRsvpMapper.toDTO(null));
    }

    // ── rsvpIdToUUID / uuidToRsvpId ───────────────────────────

    @Test
    void rsvpIdToUUID_shouldReturnUUID_whenRsvpIdNotNull() {
        UUID uuid = UUID.randomUUID();
        RsvpId rsvpId = new RsvpId(uuid);

        assertEquals(uuid, eventRsvpMapper.rsvpIdToUUID(rsvpId));
    }

    @Test
    void rsvpIdToUUID_shouldReturnNull_whenRsvpIdIsNull() {
        assertNull(eventRsvpMapper.rsvpIdToUUID(null));
    }

    @Test
    void uuidToRsvpId_shouldReturnRsvpId_whenUUIDNotNull() {
        UUID uuid = UUID.randomUUID();
        RsvpId result = eventRsvpMapper.uuidToRsvpId(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getValue());
    }

    @Test
    void uuidToRsvpId_shouldReturnNull_whenUUIDIsNull() {
        assertNull(eventRsvpMapper.uuidToRsvpId(null));
    }

    // ── eventIdToUUID / uuidToEventId ─────────────────────────

    @Test
    void eventIdToUUID_shouldReturnUUID_whenEventIdNotNull() {
        UUID uuid = UUID.randomUUID();
        EventId eventId = new EventId(uuid);

        assertEquals(uuid, eventRsvpMapper.eventIdToUUID(eventId));
    }

    @Test
    void eventIdToUUID_shouldReturnNull_whenEventIdIsNull() {
        assertNull(eventRsvpMapper.eventIdToUUID(null));
    }

    @Test
    void uuidToEventId_shouldReturnEventId_whenUUIDNotNull() {
        UUID uuid = UUID.randomUUID();
        EventId result = eventRsvpMapper.uuidToEventId(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getValue());
    }

    @Test
    void uuidToEventId_shouldReturnNull_whenUUIDIsNull() {
        assertNull(eventRsvpMapper.uuidToEventId(null));
    }
}
