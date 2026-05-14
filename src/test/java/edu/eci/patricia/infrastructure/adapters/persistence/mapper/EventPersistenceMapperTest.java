package edu.eci.patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventPersistenceMapperTest {

    private EventPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EventPersistenceMapper.class);
    }

    @Test
    void toEntity_shouldMapAllFields() {
        UUID organizerId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
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
                .availableCapacity(80)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .qrCode("qr-code-value")
                .createdAt(now)
                .updatedAt(now)
                .build();

        EventEntity entity = mapper.toEntity(event);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Tech Talk", entity.getName());
        assertEquals("A tech event", entity.getDescription());
        assertEquals(now, entity.getDateTime());
        assertEquals(90, entity.getDurationMinutes());
        assertEquals("Room 101", entity.getLocation());
        assertEquals(EventCategory.CULTURAL, entity.getCategory());
        assertEquals(EventType.OPEN, entity.getType());
        assertEquals(100, entity.getMaxCapacity());
        assertEquals(80, entity.getAvailableCapacity());
        assertEquals(EventStatus.ACTIVE, entity.getStatus());
        assertEquals(organizerId, entity.getOrganizerId());
        assertEquals("qr-code-value", entity.getQrCode());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void toModel_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        EventEntity entity = EventEntity.builder()
                .id(id)
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(now)
                .durationMinutes(90)
                .location("Room 101")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableCapacity(80)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .qrCode("qr-code-value")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Event model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(id, model.getId().getValue());
        assertEquals("Tech Talk", model.getName());
        assertEquals("A tech event", model.getDescription());
        assertEquals(now, model.getDateTime());
        assertEquals(90, model.getDurationMinutes());
        assertEquals("Room 101", model.getLocation());
        assertEquals(EventCategory.CULTURAL, model.getCategory());
        assertEquals(EventType.OPEN, model.getType());
        assertEquals(100, model.getMaxCapacity());
        assertEquals(80, model.getAvailableCapacity());
        assertEquals(EventStatus.ACTIVE, model.getStatus());
        assertEquals(organizerId, model.getOrganizerId());
        assertEquals("qr-code-value", model.getQrCode());
        assertEquals(now, model.getCreatedAt());
        assertEquals(now, model.getUpdatedAt());
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
    void toEntity_shouldHandleNullId() {
        Event event = Event.builder()
                .id(null)
                .name("No ID Event")
                .build();

        EventEntity entity = mapper.toEntity(event);

        assertNotNull(entity);
        assertNull(entity.getId());
    }

    @Test
    void toModel_shouldHandleNullId() {
        EventEntity entity = EventEntity.builder()
                .id(null)
                .name("No ID Entity")
                .build();

        Event model = mapper.toModel(entity);

        assertNotNull(model);
        assertNull(model.getId());
    }
}
