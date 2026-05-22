package edu.eci.patricia.application.mapper;

import edu.eci.patricia.application.dto.request.EventRequest;
import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.domain.exceptions.EventDomainException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.valueobjects.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    private EventMapper eventMapper;

    @BeforeEach
    void setUp() {
        eventMapper = Mappers.getMapper(EventMapper.class);
    }

    // ── toDomain ──────────────────────────────────────────────

    @Test
    void toDomain_shouldMapRequestToDomain_whenValidRequest() {
        EventRequest request = EventRequest.builder()
                .name("Caminata")
                .description("Evento de senderismo")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime("08:30")
                .duration(120)
                .location("Bogotá")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(null)
                .build();

        Event result = eventMapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Caminata", result.getName());
        assertEquals("Evento de senderismo", result.getDescription());
        assertEquals(120, result.getDurationMinutes());
        assertEquals("Bogotá", result.getLocation());
        assertEquals(EventCategory.ACADEMIC, result.getCategory());
        assertEquals(EventType.OPEN, result.getType());
        assertEquals(LocalTime.of(8, 30), result.getStartTime());
    }

    @Test
    void toDomain_shouldIgnoreId_whenMapping() {
        EventRequest request = EventRequest.builder()
                .name("Evento")
                .startTime("09:00")
                .duration(60)
                .build();

        Event result = eventMapper.toDomain(request);

        assertNull(result.getId());
        assertNull(result.getOrganizerId());
        assertNull(result.getStatus());
        assertNull(result.getQrCode());
    }

    @Test
    void toDomain_shouldMapDuration_fromDurationField() {
        EventRequest request = EventRequest.builder()
                .name("Evento")
                .startTime("10:00")
                .duration(90)
                .build();

        Event result = eventMapper.toDomain(request);

        assertEquals(90, result.getDurationMinutes());
    }

    // ── toDTO ─────────────────────────────────────────────────

    @Test
    void toDTO_shouldMapDomainToResponse_whenValidEvent() {
        UUID id = UUID.randomUUID();
        Event event = Event.builder()
                .id(new EventId(id))
                .name("Evento Test")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(9, 0))
                .durationMinutes(60)
                .location("Medellín")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .qrCode("QR-" + id)
                .build();

        EventResponse result = eventMapper.toDTO(event);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Evento Test", result.getName());
        assertEquals(60, result.getDuration());
        assertEquals(EventStatus.ACTIVE, result.getStatus());
    }

    @Test
    void toDTO_shouldMapDurationMinutes_toDuration() {
        Event event = Event.builder()
                .id(EventId.generate())
                .name("Evento")
                .durationMinutes(45)
                .build();

        EventResponse result = eventMapper.toDTO(event);

        assertEquals(45, result.getDuration());
    }

    // ── toFeedDTO ─────────────────────────────────────────────

    @Test
    void toFeedDTO_shouldMapDomainToFeedResponse_whenValidEvent() {
        UUID id = UUID.randomUUID();
        Event event = Event.builder()
                .id(new EventId(id))
                .name("Feed Evento")
                .description("Descripción")
                .dateTime(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(30)
                .location("Cali")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .availableCapacity(20)
                .status(EventStatus.ACTIVE)
                .qrCode("QR-" + id)
                .build();

        EventFeedResponse result = eventMapper.toFeedDTO(event);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Feed Evento", result.getName());
        assertEquals("Descripción", result.getDescription());
        assertEquals(20, result.getAvailableCapacity());
        assertEquals(EventStatus.ACTIVE, result.getStatus());
    }

    @Test
    void toFeedDTO_shouldMapNullAvailableCapacity_whenOpenEvent() {
        Event event = Event.builder()
                .id(EventId.generate())
                .name("Evento OPEN")
                .type(EventType.OPEN)
                .availableCapacity(null)
                .status(EventStatus.ACTIVE)
                .build();

        EventFeedResponse result = eventMapper.toFeedDTO(event);

        assertNull(result.getAvailableCapacity());
    }

    // ── stringToLocalTime ─────────────────────────────────────

    @Test
    void stringToLocalTime_shouldParseCorrectly_whenValidFormat() {
        LocalTime result = eventMapper.stringToLocalTime("08:30");
        assertEquals(LocalTime.of(8, 30), result);
    }

    @Test
    void stringToLocalTime_shouldReturnNull_whenNullInput() {
        assertNull(eventMapper.stringToLocalTime(null));
    }

    @Test
    void stringToLocalTime_shouldThrowException_whenInvalidFormat() {
        assertThrows(EventDomainException.class,
                () -> eventMapper.stringToLocalTime("8:30:00"));
    }

    @Test
    void stringToLocalTime_shouldThrowException_whenRandomString() {
        assertThrows(EventDomainException.class,
                () -> eventMapper.stringToLocalTime("not-a-time"));
    }

    // ── eventIdToUUID / uuidToEventId ─────────────────────────

    @Test
    void eventIdToUUID_shouldReturnUUID_whenEventIdNotNull() {
        UUID uuid = UUID.randomUUID();
        EventId eventId = new EventId(uuid);

        assertEquals(uuid, eventMapper.eventIdToUUID(eventId));
    }

    @Test
    void eventIdToUUID_shouldReturnNull_whenEventIdIsNull() {
        assertNull(eventMapper.eventIdToUUID(null));
    }

    @Test
    void uuidToEventId_shouldReturnEventId_whenUUIDNotNull() {
        UUID uuid = UUID.randomUUID();
        EventId result = eventMapper.uuidToEventId(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getValue());
    }

    @Test
    void uuidToEventId_shouldReturnNull_whenUUIDIsNull() {
        assertNull(eventMapper.uuidToEventId(null));
    }
}
