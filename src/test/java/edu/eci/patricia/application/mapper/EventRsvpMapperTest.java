package edu.eci.patricia.application.mapper;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpMapperTest {

    private EventRsvpMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EventRsvpMapper.class);
    }

    @Test
    void toDTO_shouldMapAllFields_whenStatusIsConfirmed() {
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

        EventResponseRsvp response = mapper.toDTO(eventRsvp);

        assertNotNull(response);
        assertEquals(rsvpUUID, response.getId());
        assertEquals(eventUUID, response.getEventId());
        assertEquals(studentId, response.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, response.getStatus());
        assertEquals(now, response.getConfirmedAt());
    }

    @Test
    void toDTO_shouldMapAllFields_whenStatusIsCancelled() {
        UUID rsvpUUID = UUID.randomUUID();
        UUID eventUUID = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        EventRsvp eventRsvp = EventRsvp.builder()
                .id(new RsvpId(rsvpUUID))
                .eventId(new EventId(eventUUID))
                .studentId(studentId)
                .status(RsvpStatus.CANCELLED)
                .confirmedAt(null)
                .cancelledAt(null)
                .build();

        EventResponseRsvp response = mapper.toDTO(eventRsvp);

        assertNotNull(response);
        assertEquals(rsvpUUID, response.getId());
        assertEquals(eventUUID, response.getEventId());
        assertEquals(RsvpStatus.CANCELLED, response.getStatus());
        assertNull(response.getConfirmedAt());
    }

    @Test
    void toDTO_shouldReturnNullIds_whenValueObjectsAreNull() {
        EventRsvp eventRsvp = EventRsvp.builder()
                .id(null)
                .eventId(null)
                .studentId(null)
                .status(RsvpStatus.CONFIRMED)
                .build();

        EventResponseRsvp response = mapper.toDTO(eventRsvp);

        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getEventId());
        assertNull(response.getStudentId());
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
