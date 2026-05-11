package edu.eci.patricia.DOWS_patricia.application.mapper;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    private EventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EventMapper.class);
    }

    @Test
    void toDomain_shouldMapRequestFields() {
        LocalDateTime future = LocalDateTime.now().plusDays(5);

        EventRequest request = EventRequest.builder()
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(future)
                .durationMinutes(60)
                .location("Auditorium A")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .maxCapacity(200)
                .build();

        Event domain = mapper.toDomain(request);

        assertNotNull(domain);
        assertEquals("Tech Talk", domain.getName());
        assertEquals("A tech event", domain.getDescription());
        assertEquals(future, domain.getDateTime());
        assertEquals(60, domain.getDurationMinutes());
        assertEquals("Auditorium A", domain.getLocation());
        assertEquals(EventCategory.CULTURAL, domain.getCategory());
        assertEquals(EventType.OPEN, domain.getType());
        assertEquals(200, domain.getMaxCapacity());
    }

    @Test
    void toDomain_shouldIgnoreManagedFields() {
        EventRequest request = EventRequest.builder()
                .name("Event")
                .dateTime(LocalDateTime.now().plusDays(1))
                .durationMinutes(30)
                .location("Room B")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .build();

        Event domain = mapper.toDomain(request);

        assertNull(domain.getId());
        assertNull(domain.getOrganizerId());
        assertNull(domain.getStatus());
        assertNull(domain.getAvailableCapacity());
        assertNull(domain.getCreatedAt());
        assertNull(domain.getUpdatedAt());
        assertNull(domain.getQrCode());
    }

    @Test
    void toDTO_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Event event = Event.builder()
                .id(new EventId(id))
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(now)
                .durationMinutes(90)
                .location("Room 101")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableCapacity(50)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .qrCode("qr-code-value")
                .createdAt(now)
                .updatedAt(now)
                .build();

        EventResponse response = mapper.toDTO(event);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Tech Talk", response.getName());
        assertEquals("A tech event", response.getDescription());
        assertEquals(now, response.getDateTime());
        assertEquals(90, response.getDurationMinutes());
        assertEquals("Room 101", response.getLocation());
        assertEquals(EventCategory.CULTURAL, response.getCategory());
        assertEquals(EventType.OPEN, response.getType());
        assertEquals(100, response.getMaxCapacity());
        assertEquals(50, response.getAvailableCapacity());
        assertEquals(EventStatus.ACTIVE, response.getStatus());
        assertEquals(organizerId, response.getOrganizerId());
        assertEquals("qr-code-value", response.getQrCode());
        assertEquals(now, response.getCreatedAt());
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

    @Test
    void toDTO_shouldReturnNullId_whenEventIdIsNull() {
        Event event = Event.builder()
                .id(null)
                .name("No ID Event")
                .build();

        EventResponse response = mapper.toDTO(event);

        assertNotNull(response);
        assertNull(response.getId());
    }
}
